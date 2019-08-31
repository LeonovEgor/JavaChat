package Lesson4.Task2.Server.NET;

import Lesson2.Messages.ChatMessage;
import Lesson2.Messages.MessageType;
import Lesson2.Server.DataBaseHelper.SQLHelper;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;

public class ClientHandler {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private ChatServer server;
    private String nick;
    private boolean isEnding = false;

    public String getNick() {
        return nick != null ? nick : "" ;
    };

    public ClientHandler(ChatServer server, Socket socket, ExecutorService executorService) {
        if (socket == null) throw new IllegalArgumentException("Socket не может быть null");
        this.socket = socket;
        this.server = server;

        try {
            this.in = new ObjectInputStream(socket.getInputStream());
            this.out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();

            executorService.execute(() -> {
                try {
                    waitForAuth(); // Ожидание авторизации
                    if (!isEnding) waitForMessage(); // Обработка сообщений
                } catch (IOException | SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    closeConnection();
                    System.out.println(String.format("Сервер завершил работу: %s", info()));
                }
            });
// было
/*            new Thread(() -> {
                try {
                    waitForAuth(); // Ожидание авторизации
                    if (!isEnding) waitForMessage(); // Обработка сообщений
                } catch (IOException | SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    closeConnection();
                    System.out.println(String.format("Сервер завершил работу: %s", info()));
                }
            }).start();*/

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Ожидание сервисных команд до авторизации
    // авторизация, регистрация или окончание работы
    private void waitForAuth() throws IOException, SQLException, ClassNotFoundException {
        while (true) {
            ChatMessage message = (ChatMessage) in.readObject();

            if (message.getMessageType().equals(MessageType.END)) {
                ending();
                break;
            }

            if (message.getMessageType().equals(MessageType.AUTH)) {
                if (auth(message)) break;
                else continue;
            }

            if (message.getMessageType().equals(MessageType.REGISTRATION)) {
                if (registration(message)) break;
                else continue;
            }
        }
    }

    // Регистрация нового пользователя
    private boolean registration(ChatMessage message) {
        try {
            boolean hasUser = SQLHelper.hasUser(message.getLogin());
            if (hasUser) {
                sendObject(new ChatMessage(MessageType.REGISTRATION_ERROR, message.getLogin(), "Логин уже занят"));
                return false;
            } else {
                boolean res = SQLHelper.AddNewUser(message.getLogin(), message.getPassHash());
                if (res) {
                    nick = message.getLogin();
                    server.sendMessage(new ChatMessage(MessageType.INFO_MESSAGE, nick,
                            String.format("Подключился новый пользователь %s", nick))); // Всем, что клиент подключился
                    server.addClient(ClientHandler.this);
                    sendObject(new ChatMessage(MessageType.REGISTRATION_OK, nick, "")); // Клиенту о том, что все в порядке
                    return true;
                } else {
                    sendObject(new ChatMessage(MessageType.REGISTRATION_ERROR,
                            message.getLogin(), "Что-то пошло не так на сервере"));
                    return false;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Ожидание сообщения или сервисных команд после авторизации
    private void waitForMessage() throws IOException {
        while (true) {
            ChatMessage message;
            try {
                message = (ChatMessage) in.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.out.println("Сообщение принято, но не может быть десериализовано.");
                continue;
            }
            System.out.println(String.format("Client %s: %s ", info(), message.getMessage()));

            if (message.getMessageType().equals(MessageType.END)) {
                ending();
                break;
            }
            if (message.getMessageType().equals(MessageType.ADD_BLOCK)){
                blocking(message);
                continue;
            }
            if (message.getMessageType().equals(MessageType.CHANGE_NICK)) {
                changeNick(message.getNickTo());
                continue;
            }
            server.sendMessage(message);
        }
    }

    // Изменение Nick пользователя
    private void changeNick(String newNick) {
        boolean res = SQLHelper.ChangeNick(this.nick, newNick);

        // сообщить остальным
        if (res) {
            String oldNick = this.nick;
            this.nick = newNick;
            server.sendMessage(new ChatMessage(MessageType.INFO_MESSAGE,
                    "Server", String.format("Пользователь %s изменил свое имя на %s", oldNick, newNick)));
            server.sendMessage(new ChatMessage(MessageType.CHANGE_NICK_OK,
                    newNick, String.format("Имя пользователя успешно изменено с %s на %s", oldNick, newNick)));
        }
        else server.sendMessage( new ChatMessage( MessageType.CHANGE_NICK_ERROR,
                "Server", "Не удалось изменить имя"));


    }

    // Блокировка пользователя другим пользователем
    private void blocking(ChatMessage message) {
        SQLHelper.AddBlock(message.getNickFrom(), message.getNickTo());
    }

    // Завершение работы
    private void ending() {
        isEnding = true;
        sendObject(new ChatMessage(MessageType.END, nick, ""));
        server.removeClient(ClientHandler.this);

        // сообщить остальным
        server.sendMessage( new ChatMessage( MessageType.INFO_MESSAGE,
                "Server", String.format("Отключился пользователь %s", nick)));
    }


    // Авторизация
    private boolean auth(ChatMessage message) throws SQLException {
        String newNick = SQLHelper.getNickByLoginAndPass(message.getLogin(), message.getPassHash());
        if (newNick != null) {
            if (server.isAlreadyConnected(newNick)) {
                sendObject(new ChatMessage(MessageType.AUTH_ERROR,
                        nick, "Повторное подключение запрещено"));
                return false;
            }

            nick = newNick;
            server.sendMessage(new ChatMessage(MessageType.INFO_MESSAGE, nick,
                    String.format("Подключился пользователь %s", nick))); // Всем, что клиент подключился
            server.addClient(ClientHandler.this);
            sendObject(new ChatMessage(MessageType.AUTH_OK, nick, "")); // Клиенту о том, что все в порядке
            return true;
        } else {
            sendObject(new ChatMessage(MessageType.AUTH_ERROR,
                    message.getLogin(), "Неверный логин или пароль"));
            return false;
        }
    }

    private void closeConnection() {
        try {
            if (in != null) in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (out != null) out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void sendObject(ChatMessage message) {
        try {
            out.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String info() {
        return socket != null ?
                String.format("Клиент: адрес: %s ник: %s", socket.getInetAddress(), nick)
                : "Socket не создан!";
    }
}

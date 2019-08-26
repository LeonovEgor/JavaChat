package Lesson2.Server.NET;

import Lesson2.Messages.ChatMessage;
import Lesson2.Messages.MessageType;
import Lesson2.Server.DataBaseHelper.SQLHelper;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

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

    public ClientHandler(ChatServer server, Socket socket) {
        if (socket == null) throw new IllegalArgumentException("Socket не может быть null");
        this.socket = socket;
        this.server = server;

        try {
            this.in = new ObjectInputStream(socket.getInputStream());
            this.out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();

            new Thread(() -> {
                try {
                    waitForAuth();
                    if (!isEnding) waitForMessage();
                } catch (IOException | SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    closeConnection();
                    System.out.println(String.format("Соединение  закрыто: %s", info()));
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
                blockng(message);
                break;
            }
            server.sendMessage(message);
        }
    }

    private void blockng(ChatMessage message) {
        SQLHelper.AddBlock(message.getNickFrom(), message.getNickTo());
    }

    private void ending() {
        isEnding = true;
        sendObject(new ChatMessage(MessageType.END, nick, ""));
        server.removeClient(ClientHandler.this);

        // сообщить остальным
        server.sendMessage(
                new ChatMessage(
                        MessageType.INFO_MESSAGE,
                        "Server",
                        String.format("Отключился пользователь %s", nick)
                )
        );
    }

    private void waitForAuth() throws IOException, SQLException, ClassNotFoundException {
        while (true) {
            ChatMessage message = (ChatMessage) in.readObject();

            if (message.getMessageType().equals(MessageType.END)) {
                ending();
                break;
            }

            if (message.getMessageType().equals(MessageType.AUTH)) {
                String newNick = SQLHelper.getNickByLoginAndPass(message.getLogin(), message.getPassHash());
                if (newNick != null) {
                    if (server.isAlreadyConnected(newNick)) {
                        sendObject(new ChatMessage(MessageType.AUTH_ERROR,
                                nick, "Повторное подключение запрещено"));
                        continue;
                    }

                    nick = newNick;
                    server.sendMessage(new ChatMessage(MessageType.INFO_MESSAGE, nick,
                            String.format("Подключился пользователь %s", nick))); // Всем, что клиент подключился
                    server.addClient(ClientHandler.this);
                    sendObject(new ChatMessage(MessageType.AUTH_OK, nick, "")); // Клиенту о том, что все в порядке
                    break;
                } else {
                    sendObject(new ChatMessage(MessageType.AUTH_ERROR,
                            message.getLogin(), "Неверный логин или пароль"));
                }
            }
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

    public void sendObject(ChatMessage message) {
        try {
            out.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String info() {
        return socket != null ?
                String.format("Клиент: адрес: %s ник: %s", socket.getInetAddress(), nick)
                : "Socket не создан!";
    }
}

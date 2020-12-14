package ru.leonov.server.net;

import ru.leonov.messages.ChatMessage;
import ru.leonov.messages.MessageType;
import ru.leonov.server.databaseHelper.SQLHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
    private static final Logger log = LoggerFactory.getLogger(ChatServer.class.getName());

    private static final int USERS_PARALLEL = 100;
    private Vector<ClientHandler> clients;

    // число пользующихся чатом одновременно ограничено 100
    private ExecutorService executorService = Executors.newFixedThreadPool(USERS_PARALLEL);
    // Если мы захотим не ограничивать число одновременно пользующихся чатом
    //ExecutorService executorService = Executors.newCachedThreadPool();
    private ServerSocket serverSocket = null;
    private Socket socket = null;

    public ChatServer(int port) throws SQLException {
        clients = new Vector<>();

        try {
            SQLHelper.connect();
            log.debug("База данных подключена");
            serverSocket = new ServerSocket(port);
            log.debug("Сервер запущен, ожидаем подключения...");

            while (true) {
                socket = serverSocket.accept();
                log.debug("Поступил запрос на новое подключение " + socket.getInetAddress() + " порт " + socket.getPort());
                new ClientHandler(this, socket, executorService);
            }
        } catch (IOException e) {
            log.error("Ошибка сети ", e);
        } finally {
            executorService.shutdown();
            closeServer();
            SQLHelper.disconnect();
        }
    }

    private void closeServer() {
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
            log.error("Ошибка закрытия socket", e);
        }

        try {
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            log.error("Ошибка закрытия serverSocket", e);
        }
    }

    public void sendMessage(ChatMessage message) {
        switch (message.getMessageType()) {
            case PRIVATE_MESSAGE: // личные сообщения
                ClientHandler handler = getClient(message.getNickTo());
                if (handler != null) {
                    try {
                        if (!SQLHelper.isBlockedUser(message.getNickTo(), message.getNickFrom()))
                            handler.sendObject(message); // тому, кому назначено
                        else {
                            log.debug(
                                    String.format("%s не хочет видеть сообдения от %s. Сообщение не будет отправлено",
                                            message.getNickTo(), message.getNickFrom()));
                        }
                    } catch (SQLException e) {
                        log.error("Ошибка проверки блокировки пользователя", e);
                    }
                    handler = getClient(message.getNickFrom());
                    handler.sendObject(message); // себе
                }
                else {
                    handler = getClient(message.getNickFrom());
                    handler.sendObject(new ChatMessage(message.getDate(), message.getNickFrom(), message.getNickTo(),
                            MessageType.ERROR_MESSAGE, "Пользователь с таким ником не подключен")); // себе
                }
                break;
            case CHANGE_NICK_OK:
            case CHANGE_NICK_ERROR:
                handler = getClient(message.getNickFrom());
                handler.sendObject(message); // себе
                break;
            case BROADCAST_MESSAGE: // сообщения всем
                for (ClientHandler ch: clients) {
                    try {
                        if (!SQLHelper.isBlockedUser(ch.getNick(), message.getNickFrom()))
                            ch.sendObject(message);
                        else {
                            log.debug(
                                    String.format("%s не хочет видеть сообдения от %s. Сообщение не будет отправлено",
                                            ch.getNick(), message.getNickFrom()));
                        }

                    } catch (SQLException e) {
                        log.error("Ошибка проверки блокировки пользователя", e);
                        e.printStackTrace();
                    }
                }
                break;
            case INFO_MESSAGE: // сообщения от сервера (клиен подключился/отключился ...)
                for (ClientHandler ch: clients) {
                    ch.sendObject(message);
                }

                break;
        }
    }

    void addClient(ClientHandler clientHandler) {
        clients.add(clientHandler);
        log.debug("Подключился клиент " + clientHandler.info());
        log.debug(String.format("В текущий момент клиентов %d: %s", clients.size(), getClientListText()));
    }

    void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        log.debug("Отключился клиент " + clientHandler.info());
        log.debug(String.format("Осталось клиентов %d: %s", clients.size(), getClientListText()));
    }

    boolean isAlreadyConnected(String nick) {
        boolean result = false;

        for (ClientHandler handler: clients) {
            if (handler.getNick().equals(nick)) {
                result = true;
                break;
            }
        }
        return result;
    }

    private String getClientListText() {
        StringBuilder sb = new StringBuilder();
        for (ClientHandler handler: clients) {
            sb.append(handler.info()).append(", ");
        }
        return sb.toString();
    }

    private ClientHandler getClient(String privateName) {
        ClientHandler result = null;
        for (ClientHandler handler: clients) {
            if (handler.getNick().equals(privateName)) {
                result = handler;
                break;
            }
        }
        return result;
    }

}
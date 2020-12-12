package ru.leonov.client;

import ru.leonov.client.actions.AuthListener;
import ru.leonov.client.actions.AuthListenersRegistrator;
import ru.leonov.client.actions.MessageListenersRegistrator;
import ru.leonov.client.fx.ui.Controller;
import ru.leonov.client.net.Client;
import ru.leonov.client.storage.StorageHelper;
import ru.leonov.messages.ChatMessage;
import ru.leonov.messages.MessageType;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class Main extends Application implements AuthListener {
    private static final int WIDTH = 530;
    private static final int HEIGHT = 600;
    private static final int MIN_HEIGHT = 300;
    private static final int MIN_WIDTH = 200;

    private final String SERVER_ADDR = "localhost";
    private final int SERVER_PORT = 8189;

    private Client client;
    private Stage primaryStage;
    private Controller controller;

    private String nick;

    private MessageListenersRegistrator messageListenersRegistrator;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;

        URL url = getClass().getResource("/fxml/Chat.fxml");
        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();
        primaryStage.setTitle("Simple Messenger");
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setMinWidth(MIN_WIDTH);

        //////
        messageListenersRegistrator = new MessageListenersRegistrator();
        AuthListenersRegistrator authListenersRegistrator = new AuthListenersRegistrator();

        client = new Client(messageListenersRegistrator, authListenersRegistrator);
        try {
            client.openConnection(SERVER_ADDR, SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        controller = loader.getController();
        controller.setAuthorized(false);
        messageListenersRegistrator.addListener(controller);


        authListenersRegistrator.addListener(controller);
        authListenersRegistrator.addListener(this);
        controller.setSender(client);

        ///////

        primaryStage.show();

        // Предотвращение закрытия окна для выхода с оповещением сервера
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            client.sendObject(new ChatMessage(MessageType.END, nick, ""));
        });
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void AuthListenerPerformAction(String nick) {
        Platform.runLater((() -> primaryStage.setTitle("Simple Messenger - " + nick)));
        this.nick = nick;
        controller.setNick(nick);
        StorageHelper storageHelper = new StorageHelper("message_"+nick+".msg");
        Platform.runLater(( () -> {
            ArrayList<ChatMessage> list = storageHelper.getMessage();
            controller.setHistory(list);
        }));
        messageListenersRegistrator.addListener(storageHelper);
    }

}
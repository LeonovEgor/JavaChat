package Lesson2.Client;

import Lesson2.Client.Actions.AuthListener;
import Lesson2.Client.Actions.AuthListenersRegistrator;
import Lesson2.Client.Actions.MessageListenersRegistrator;
import Lesson2.Client.FXUI.Controller;
import Lesson2.Client.NET.Client;
import Lesson2.Messages.ChatMessage;
import Lesson2.Messages.MessageType;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

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

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Lesson2/Client/FXUI/Chat.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Simple Messenger");
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setMinWidth(MIN_WIDTH);

        //////
        MessageListenersRegistrator messageListenersRegistrator = new MessageListenersRegistrator();
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
    public void alPerformAction(String nick) {
        Platform.runLater((() -> primaryStage.setTitle("Simple Messenger - " + nick)));
        this.nick = nick;
        controller.setNick(nick);
    }
}

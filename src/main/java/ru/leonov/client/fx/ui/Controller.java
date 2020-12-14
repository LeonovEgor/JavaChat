package ru.leonov.client.fx.ui;

import ru.leonov.client.actions.AuthListener;
import ru.leonov.client.actions.MessageListener;
import ru.leonov.client.fx.utils.AlertHelper;
import ru.leonov.messages.ChatMessage;
import ru.leonov.client.net.MessageSendable;
import ru.leonov.messages.MessageType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements MessageListener, AuthListener, Initializable {

    @FXML
    ListView<ChatMessage> lvHistory;

    @FXML
    TextField tfMessage;

    @FXML
    Button btnSend;

    @FXML
    HBox messagePanel;

    @FXML
    HBox authPanel;

    @FXML
    TextField loginField;

    @FXML
    PasswordField passwordField;

    public String nick;

    private MessageSendable sender;

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setAuthorized(boolean isAuthorized) {
        authPanel.setVisible(!isAuthorized);
        authPanel.setManaged(!isAuthorized);
        messagePanel.setVisible(isAuthorized);
        messagePanel.setManaged(isAuthorized);
    }

    public void setSender(MessageSendable sender) {
        this.sender = sender;
    }

    public void sendMessage() {
        if (tfMessage.getText().trim().isEmpty()) return;

        boolean res = sender.sendMessage(tfMessage.getText());
        if (!res) {
            AlertHelper.ShowMessage(Alert.AlertType.WARNING,
                    "Проблема", "Не удалось отправить сообщение");
        }
        else {
            tfMessage.clear();
            tfMessage.requestFocus();
        }
    }

    public void tryToAuth() {
        if (!sender.isAuthorized())
            sender.Auth(loginField.getText(), passwordField.getText().hashCode());
    }

    public void registration(ActionEvent actionEvent) {
        sender.registration(loginField.getText(), passwordField.getText().hashCode());
    }

    @Override
    public void messageListenerPerformAction(ChatMessage message) {
        if (message.getMessageType().equals(MessageType.END)) {
            System.out.println("Приложение закрывается");
            Platform.exit();
        }
        else {
            Platform.runLater(() -> lvHistory.getItems().add(message));
        }
    }

    @Override
    public void AuthListenerPerformAction(String nick) {
        setAuthorized(true);
        lvHistory.setCellFactory(chatListView -> new ChatListViewCell(nick));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lvHistory.setCellFactory(chatListView -> new ChatListViewCell(nick));
    }

    public void setHistory(ArrayList<ChatMessage> list) {
        lvHistory.getItems().addAll(list);
    }

}
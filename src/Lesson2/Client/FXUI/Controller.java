package Lesson2.Client.FXUI;

import Lesson2.Client.Actions.AuthListener;
import Lesson2.Client.Actions.MessageListener;
import Lesson2.Client.FXUtils.AlertHelper;
import Lesson2.Messages.ChatMessage;
import Lesson2.Client.NET.MessageSendable;
import Lesson2.Messages.MessageType;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements MessageListener, AuthListener, Initializable {

    @FXML
    ListView<ChatMessage> lvHistory;

    @FXML
    TextField tfMessage;

    @FXML
    Button btnSend;

    @FXML
    HBox bottomPanel;

    @FXML
    HBox upperPanel;

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
        if (!isAuthorized) {
            upperPanel.setVisible(true);
            upperPanel.setManaged(true);
            bottomPanel.setVisible(false);
            bottomPanel.setManaged(false);
        } else {
            upperPanel.setVisible(false);
            upperPanel.setManaged(false);
            bottomPanel.setVisible(true);
            bottomPanel.setManaged(true);
        }
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

    @Override
    public void mlPerformAction(ChatMessage message) {
        if (message.getMessageType().equals(MessageType.END)) {
            System.out.println("Приложение закрывается");
            Platform.exit();
        }
        else {
            Platform.runLater(() -> lvHistory.getItems().add(message));
        }
    }

    @Override
    public void alPerformAction(String nick) {
        setAuthorized(true);
        lvHistory.setCellFactory(chatListView -> new ChatListViewCell(nick));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lvHistory.setCellFactory(chatListView -> new ChatListViewCell(nick));
    }
}

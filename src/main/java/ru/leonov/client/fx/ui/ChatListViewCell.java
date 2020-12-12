package ru.leonov.client.fx.ui;


import ru.leonov.messages.ChatMessage;
import ru.leonov.messages.MessageType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class ChatListViewCell extends ListCell<ChatMessage> {

    @FXML
    private Label lblNickFrom;

    @FXML
    private Label lblNickTo;

    @FXML
    private Label lblTime;

    @FXML
    private Label lblMessage;

    @FXML
    private VBox vBoxCell;

    @FXML
    private HBox captionPanel;

    private FXMLLoader mLLoader;

    private final String nick;

    public ChatListViewCell(String nick) {

        if (mLLoader == null) {
            mLLoader = new FXMLLoader(getClass().getResource("/fxml/ChatCell.fxml"));
            mLLoader.setController(this);

            try {
                mLLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.nick = nick;
        this.lblNickFrom.setText("");
        this.lblTime.setText("");
        this.lblMessage.setText("");
        this.lblNickTo.setText("");
    }

    @Override
    protected void updateItem(ChatMessage message, boolean empty) {
        super.updateItem(message, empty);

        if(empty || message == null) {
            setText(null);
            setGraphic(null);

        } else {
            lblNickFrom.setText(message.getNickFrom());
            lblNickTo.setText(message.getNickTo().isEmpty() ? "Всем" : message.getNickTo());
            lblTime.setText(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(message.getDate()));
            lblMessage.setText(message.getMessage());

            if (message.getMessageType().equals(MessageType.BROADCAST_MESSAGE) ||
                    message.getMessageType().equals(MessageType.PRIVATE_MESSAGE)) { // сообщения
                if (message.getNickFrom().equals(nick)) { // свои
                    vBoxCell.setAlignment(Pos.CENTER_RIGHT);
                    vBoxCell.setStyle("-fx-background-color: rgba(255, 165, 0, 0.3);");
                    captionPanel.setAlignment(Pos.CENTER_RIGHT);
                } else { // чужие
                    vBoxCell.setAlignment(Pos.CENTER_LEFT);
                    vBoxCell.setStyle("-fx-background-color: rgba(255, 255, 255, 0.3);");
                    captionPanel.setAlignment(Pos.CENTER_LEFT);
                }
            }
            else { // технические
                vBoxCell.setAlignment(Pos.CENTER_RIGHT);
                vBoxCell.setStyle("-fx-background-color: rgba(0,157,253, 0.3);");
                captionPanel.setAlignment(Pos.CENTER_RIGHT);
            }

            setText(null);
            setGraphic(vBoxCell);
        }
    }
}

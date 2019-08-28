package Lesson3.Client.Storage;

import Lesson2.Client.Actions.MessageListener;
import Lesson2.Messages.ChatMessage;
import Lesson2.Messages.MessageType;
import javafx.application.Platform;

public class SaverListener implements MessageListener {
    String fileName;

    SaverListener(String fileName) {
        fileName = fileName;
    }

    @Override
    public void mlPerformAction(ChatMessage message) {
        Saver.saveMessage(fileName, message);
    }

}

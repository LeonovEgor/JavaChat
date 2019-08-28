package Lesson3.Client.Storage;

import Lesson2.Client.Actions.MessageListener;
import Lesson2.Messages.ChatMessage;

import java.util.ArrayList;

public class StorageHelper implements MessageListener {
    String fileName;
    Storage<ChatMessage> storage;

    public StorageHelper(String fileName) {
        this.fileName = fileName;
        storage = new Storage<>(fileName);
    }

    public ArrayList<ChatMessage> getMessage() {
        return storage.readMessage();
    }

    @Override
    public void mlPerformAction(ChatMessage message) {
        storage.saveMessage(message);
    }


}

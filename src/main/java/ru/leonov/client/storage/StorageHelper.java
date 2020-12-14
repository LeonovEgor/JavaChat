package ru.leonov.client.storage;

import ru.leonov.client.actions.MessageListener;
import ru.leonov.messages.ChatMessage;
import ru.leonov.messages.MessageType;

import java.util.ArrayList;

public class StorageHelper implements MessageListener {
    private final Storage<ChatMessage> storage;

    public StorageHelper(String fileName) {
        storage = new Storage<>(fileName);
    }

    public ArrayList<ChatMessage> getMessage() {
        ArrayList<ChatMessage> source = storage.readMessage();
        ArrayList<ChatMessage> dest = new ArrayList<>();
        dest.addAll(source.size() < 100 ? 0: source.size()-100, source);
        return dest;
    }

    @Override
    public void messageListenerPerformAction(ChatMessage message) {
        if (message.getMessageType().equals(MessageType.BROADCAST_MESSAGE) ||
                message.getMessageType().equals(MessageType.PRIVATE_MESSAGE)) {
            boolean res = storage.saveMessage(message);
            if (!res) System.out.println("Не удалось сохранить сообщение");
        }
        else System.out.println("Сервисное сообщение. Сохранение не требуется.");
    }

}
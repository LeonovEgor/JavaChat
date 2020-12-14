package ru.leonov.client.actions;

import ru.leonov.messages.ChatMessage;

public interface MessageListener {
    void messageListenerPerformAction(ChatMessage message);
}
package Lesson2.Client.Actions;

import Lesson2.Messages.ChatMessage;

public interface MessageListener {
    void mlPerformAction(ChatMessage message);
}

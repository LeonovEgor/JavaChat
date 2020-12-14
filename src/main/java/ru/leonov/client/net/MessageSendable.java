package ru.leonov.client.net;

public interface MessageSendable {
    boolean isAuthorized();
    boolean sendMessage(String message);
    void Auth(String login, int pass);
    void registration(String text, int hashCode);
}
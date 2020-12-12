package ru.leonov.client.net;

public interface MessageSendable {
    public boolean isAuthorized();
    public boolean sendMessage(String message);
    public void Auth(String login, int pass);
    public void registration(String text, int hashCode);
}
package Lesson2.Client.NET;

public interface MessageSendable {
    public boolean isAuthorized();
    public boolean sendMessage(String message);
    public void Auth(String login, int pass);
}

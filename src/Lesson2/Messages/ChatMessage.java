package Lesson2.Messages;

import java.io.Serializable;
import java.util.Date;

public class ChatMessage implements Serializable {

    private Date date;
    private String nickFrom;
    private String nickTo;
    private String message;
    private MessageType messageType;
    private String login;
    private int passHash;

    public ChatMessage(Date date, String nickFrom, String nickTo, MessageType messageType, String message) {
        this.date = date;
        this.nickFrom = nickFrom;
        this.nickTo = nickTo;
        this.message = message;
        this.messageType = messageType;
    }


    /** Конструктор для сервисных сообщений
     * @param messageType - тип сообщения
     * @param nick - Отправитель или получатель сервисного сообщения (не важно)
     * @param message - сообщение
     */
    public ChatMessage(MessageType messageType, String nick, String message) {
        this(new Date(), nick, nick, messageType, message);
    }

    public ChatMessage(String login, int passHash, boolean newUser) {
        this.date = new Date();
        if (newUser) this.messageType = MessageType.REGISTRATION;
        else this.messageType = MessageType.AUTH;
        this.login = login;
        this.passHash = passHash;
    }

    public ChatMessage(MessageType type, String newNick) {
        this.date = new Date();
        this.messageType = type;
        this.nickFrom = newNick;
        this.nickTo = newNick;
    }

    public Date getDate() {
        return date;
    }

    public String getNickFrom() {
        return nickFrom;
    }

    public String getNickTo() {
        return nickTo;
    }

    public String getMessage() {
        return message;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public String getLogin() {
        return login;
    }

    public int getPassHash() {
        return passHash;
    }
}



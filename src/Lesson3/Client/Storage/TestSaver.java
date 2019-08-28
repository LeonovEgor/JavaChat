package Lesson3.Client.Storage;

import Lesson2.Messages.ChatMessage;
import Lesson2.Messages.MessageType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class TestSaver {
    public static void main(String[] args) {
        Saver saver = new Saver();

        for(int i=0; i<100; i++) {
            ChatMessage message = new ChatMessage(new Date(), "Maria"+i, "Vasia"+i,
                    MessageType.PRIVATE_MESSAGE, "test"+i  );
            try {
                saver.saveMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ArrayList<ChatMessage> list = saver.readMessage();
    }

}

package Lesson3.Client.Storage;

import Lesson2.Messages.ChatMessage;
import Lesson2.Messages.MessageType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class TestStorage {
    private static String fileName = "message_111.mgs";
/*
    public static void main(String[] args) {
        boolean res;
        Storage<ChatMessage> saver = new Storage<>(fileName);
        for(int i=0; i<100; i++) {
            ChatMessage message = new ChatMessage(new Date(), "Maria"+i, "Vasia"+i,
                    MessageType.PRIVATE_MESSAGE, "test"+i  );
            res = saver.saveMessage(message);
            if (res) System.out.println("Ошибка записи сообщения в файл " + fileName);
        }

        ArrayList<ChatMessage> list = saver.readMessage();
        System.out.println(Arrays.toString(list.toArray()));
    }
*/

    public static void main(String[] args) {
        Storage<ChatMessage> saver = new Storage<>("message_Марина.msg");
        ArrayList<ChatMessage> list = saver.readMessage();
        System.out.println(Arrays.toString(list.toArray()));
    }


}

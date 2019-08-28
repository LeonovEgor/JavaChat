package Lesson3.Client.Storage;

import Lesson2.Messages.ChatMessage;

import java.io.*;
import java.util.ArrayList;

class Saver {


    static boolean saveMessage(String fileName, ChatMessage message)  {
        boolean res;
        File file = new File(fileName);
        if (file.exists()) {
            try {
                FileOutputStream fos = new FileOutputStream(file, true);
                ObjectOutputStream out = new ObjectOutputStream(fos);
                out.writeObject(message);
                out.flush();
                out.close();
                res = true;
            } catch (IOException e) {
                e.printStackTrace();
                res = false;
            }
        }
        else {
            System.out.println("Файл не найден: " + fileName);
            res = false;
        }

        return res;
    }

    static ArrayList<ChatMessage> readMessage(String fileName) {
        ArrayList<ChatMessage> list = new ArrayList<>();
        try {
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);

            //list.add((ChatMessage) in.readObject());
            System.out.println(in.readObject().toString());
            in.close();
            fileIn.close();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}

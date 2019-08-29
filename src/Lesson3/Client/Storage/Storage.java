package Lesson3.Client.Storage;

import Lesson2.Messages.ChatMessage;

import java.io.*;
import java.util.ArrayList;

class Storage<T> {
    private String fileName;

    Storage(String fileName) {
        this.fileName = fileName;
    }

    boolean saveMessage(T object)  {
        boolean res = false;
        File file = new File(fileName);
        try {
            ObjectOutputStream out  = file.exists() ?
                    new AppendingObjectOutputStream(new FileOutputStream(fileName, true))
                    : new ObjectOutputStream(new FileOutputStream(fileName, true));

            out.writeObject(object);
            out.flush();
            out.close();
            res = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    ArrayList<ChatMessage> readMessage() {
        ArrayList<ChatMessage> list = new ArrayList<>();

        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("Нет данных для загрузки. Файл отсутствует: " + fileName);
        }
        else {
            ObjectInputStream in = null;
               try {
                   in = new ObjectInputStream(new FileInputStream(fileName));

                   Object obj;
                   while (true) {
                       obj = in.readObject();
                       if (obj == null) break;

                       list.add((ChatMessage)obj);
                       System.out.println(obj.toString());
                   }
                   in.close();
               } catch (EOFException ex) {
                   //
               }
               catch (ClassNotFoundException | IOException e) {
                   e.printStackTrace();
               }
        }
        return list;
    }
}

package Lesson3.Client.Storage;

import Lesson2.Messages.ChatMessage;

import java.io.*;
import java.util.ArrayList;

class Storage<T> {
    private String fileName;

    Storage(String fileName) {
        this.fileName = fileName;
    }

    boolean  saveMessage(T object)  {
        boolean res = false;
        boolean fileExists = false;
        File file = new File(fileName);
        if (file.exists()) fileExists = true;
        try {
            //FileOutputStream fos = new FileOutputStream(file, true);
            ObjectOutputStream out  = fileExists ? new AppendingObjectOutputStream(new FileOutputStream(fileName, true))
                : new ObjectOutputStream(new FileOutputStream(fileName, true));


            if (!fileExists)
            //AppendingObjectOutputStream out = new AppendingObjectOutputStream(fos);

            //if (!fileExists) out.writeStreamHeader();

            out.flush();
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
            System.out.println("Файл не найден: " + fileName);
        }
        else {
               try {
                   FileInputStream fileIn = new FileInputStream(fileName);
                   ObjectInputStream in = new ObjectInputStream(fileIn);

                   Object obj;
                   while (true) {
                       obj = in.readObject();
                       if (obj == null) break;

                       list.add((ChatMessage)obj);
                       System.out.println(obj.toString());
                   }
                   in.close();
                   fileIn.close();
               } catch (ClassNotFoundException | IOException e) {
                   e.printStackTrace();
               }
        }
        return list;
    }
}

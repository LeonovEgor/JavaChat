package Lesson4.Task2.Server;


import Lesson4.Task2.Server.NET.ChatServer;

import java.sql.SQLException;

public class Main {
    private static final int PORT = 8189;

    public static void main(String[] args) {
        try {
            new ChatServer(PORT);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

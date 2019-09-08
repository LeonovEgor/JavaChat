package Lesson6.Task1.Server;


import Lesson6.Task1.Server.NET.ChatServer;

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

package ru.leonov.server;


import ru.leonov.server.net.ChatServer;

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
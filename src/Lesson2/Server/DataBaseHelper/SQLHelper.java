package Lesson2.Server.DataBaseHelper;;

import java.sql.*;

public class SQLHelper {
    private static final String NICK_NAME = "nickname";
    private static Connection connection;
    private static Statement stmt;

    public static void connect() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:main.db");
            stmt = connection.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getNickByLoginAndPass(String login, int passHash) throws SQLException {

        String qry = String.format("SELECT nickname FROM users where login = '%s' and password = %d", login, passHash);
        ResultSet rs = stmt.executeQuery(qry);

        if (rs.next()) {
            return rs.getString(NICK_NAME);
        }

        return null;
    }

    private static int getIdByNick(String nick) throws SQLException {

        String qry = String.format("SELECT * FROM users WHERE nickname = '%s'", nick);
        ResultSet rs = stmt.executeQuery(qry);

        if (rs.next()) return rs.getInt("id");

        return -1;
    }

    public static boolean isBlockedUser(String nick, String checkedNick) throws SQLException {

        int userId = getIdByNick(nick);
        int checkedId = getIdByNick(checkedNick);

        String qry = String.format(
                "SELECT * FROM users " +
                        "JOIN BlackList ON users.id = BlackList.UserId " +
                        "WHERE users.id = %d AND BlackList.BlockedId = %d", userId, checkedId);
        ResultSet rs = stmt.executeQuery(qry);

        return rs.next();
    }

    public static void AddBlock(String nickFrom, String nickTo) {
        try {
            if (isBlockedUser(nickFrom, nickTo)) {
                System.out.println("Блокировка не требуется");
                return;
            }

            int userId = getIdByNick(nickFrom);
            int IdToBlock = getIdByNick(nickTo);

            String qry = String.format("INSERT INTO BlackList (UserId, BlockedId) VALUES (%d, %d)", userId, IdToBlock);
            stmt.execute(qry);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

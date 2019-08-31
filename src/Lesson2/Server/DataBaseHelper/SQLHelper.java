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

        return rs.next() ? rs.getString(NICK_NAME) : null;
    }

    private static int getIdByNick(String nick) throws SQLException {

        String qry = String.format("SELECT * FROM users WHERE nickname = '%s'", nick);
        ResultSet rs = stmt.executeQuery(qry);

        return rs.next() ? rs.getInt("id") : -1;
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

    public static boolean hasUser(String login) throws SQLException {

        String qry = String.format("SELECT * FROM users WHERE users.login = '%s' ", login);
        ResultSet rs = stmt.executeQuery(qry);

        return rs.next();
    }

    public static boolean hasUserByNick(String nick) throws SQLException {

        String qry = String.format("SELECT * FROM users WHERE users.nickname = '%s' ", nick);
        ResultSet rs = stmt.executeQuery(qry);

        return rs.next();
    }

    public static boolean AddNewUser(String login, int passHash) {

        try {
            int id = getMaxId() + 1;
            if (id == -1) return false;
            String qry = String.format("INSERT INTO users (id, login, password, nickname) " +
                    " VALUES (%d, '%s', %d, '%s')", id, login, passHash, login);
            stmt.execute(qry);

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static int getMaxId() throws SQLException {
        String qry = "SELECT MAX(id) as id FROM users";
        ResultSet rs = stmt.executeQuery(qry);

        return rs.next() ? rs.getInt("id") : -1;
    }


    public static boolean ChangeNick(String currentNick, String newNick) {
        boolean res;
        try {
            if (!hasUserByNick(currentNick)) res = false;
            else {
                String qry = String.format("UPDATE users SET nickname = '%s' WHERE nickname = '%s'", newNick, currentNick);
                stmt.execute(qry);
                res = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            res = false;
        }
        return res;
    }
}

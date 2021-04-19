package server;

import java.sql.*;

public class DataBaseHandler {
    private static Connection connection;
    private static Statement stmt;
    private static PreparedStatement preparedStatementGetNickname;
    private static PreparedStatement preparedStatementReg;
    private static PreparedStatement preparedStatementChangeNickname;

    public static boolean connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:main.db");
            prepareAllStatements();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void prepareAllStatements() throws SQLException {
        preparedStatementGetNickname = connection.prepareStatement("SELECT nick FROM users_chat WHERE login = ? AND pass = ?;");
        preparedStatementReg = connection.prepareStatement("INSERT INTO users_chat(login, pass, nick) VALUES (?,?,?);");
        preparedStatementChangeNickname = connection.prepareStatement("UPDATE users_chat SET nick = ? WHERE nick = ?;");
    }

    public static String getNicknameByLoginAndPassword(String login, String pass) {
        String nick = null;
        try {
            preparedStatementGetNickname.setString(1, login);
            preparedStatementGetNickname.setString(2, pass);
            ResultSet rs = preparedStatementGetNickname.executeQuery();
            if (rs.next()) {
                nick = rs.getString(1);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nick;
    }

    public static boolean registration(String login, String pass, String nick) {
        try {
            preparedStatementReg.setString(1, login);
            preparedStatementReg.setString(2, pass);
            preparedStatementReg.setString(3, nick);
            preparedStatementReg.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

//    public static boolean changeNick(String oldNick, String newNick) {
//        try {
//            preparedStatementChangeNickname.setString(1, oldNick);
//            preparedStatementChangeNickname.setString(2, newNick);
//            preparedStatementGetNickname.executeUpdate();
//            return true;
//        } catch (SQLException e) {
//            return false;
//        }
//    }

    public static void disconnect() {
        try {
            preparedStatementGetNickname.close();
            preparedStatementChangeNickname.close();
            preparedStatementReg.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
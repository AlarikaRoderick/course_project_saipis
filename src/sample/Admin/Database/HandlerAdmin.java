package sample.Admin.Database;

import sample.Admin.Admin;
import sample.User.Database.Configs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class HandlerAdmin extends Configs {
    Connection dbConnection;

    public Connection getDbConnection()
            throws ClassNotFoundException, SQLException {
        String ConnectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName + "?autoReconnect=true&useSSL=false";

        Class.forName("com.mysql.jdbc.Driver");
        dbConnection = DriverManager.getConnection(ConnectionString, dbUser, dbPass);

        return dbConnection;
    }

    public void SignUpAdmin(Admin admin){
        String insert = "INSERT INTO " + ConstAdmin.ADMIN_TABLE + "(" + ConstAdmin.ADMIN_USERNAME +"," +
                ConstAdmin.ADMIN_PASSWORD + ")" + "VALUES(?,?)";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setString(1, admin.getLogin());
            prSt.setString(2, admin.getPassword());
            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getAdmin(Admin admin){
        ResultSet resSet = null;

        String select = "SELECT * FROM " + ConstAdmin.ADMIN_TABLE + " WHERE " + ConstAdmin.ADMIN_USERNAME + "=? AND " +
                ConstAdmin.ADMIN_PASSWORD + "=?";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1, admin.getLogin());
            prSt.setString(2, admin.getPassword());
            resSet = prSt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return resSet;
    }
}

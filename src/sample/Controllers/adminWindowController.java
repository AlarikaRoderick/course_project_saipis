package sample.Controllers;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.Observable;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import sample.User.Database.Configs;
import sample.User.Database.Const;
import sample.User.User;
import sample.User.UserTV;
import sample.alternative.Alternative;
import sample.alternative.Database.ConstAlternative;

public class adminWindowController extends Configs implements Initializable{

    private final ObservableList<Alternative> data = FXCollections.observableArrayList();
    private final ObservableList<UserTV> users = FXCollections.observableArrayList();
    private Connection dbConnection = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Alternative> tableAlternative;

    @FXML
    private TableColumn<Alternative, String> idAlternative;

    @FXML
    private TableColumn<Alternative, String> textAlternative;

    @FXML
    private TableView<UserTV> tableUser;

    @FXML
    private TableColumn<UserTV, String> idUser;

    @FXML
    private TableColumn<UserTV, String> loginUser;

    @FXML
    private TableColumn<UserTV, String> passwordUser;

    @FXML
    private TextField alternativeField;

    @FXML
    private TextField numbAlternativeField;

    @FXML
    private Button addAlternativeButton;

    @FXML
    private Button deleteAlternativeButton;

    @FXML
    private TextField userLoginField;

    @FXML
    private TextField userPasswordField;

    @FXML
    private Button addUserButton;

    @FXML
    private TextField numbUserField;

    @FXML
    private Button deleteUserButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try{
            idAlternative.setCellValueFactory(cell -> cell.getValue().idAltProperty());
            textAlternative.setCellValueFactory(cell -> cell.getValue().textAltProperty());
            PreInit();
            tableAlternative.setItems(data);

        }catch(SQLException e){
            e.printStackTrace();
        }

        try{
            idUser.setCellValueFactory(cell -> cell.getValue().idUserProperty());
            loginUser.setCellValueFactory(cell -> cell.getValue().userLoginProperty());
            passwordUser.setCellValueFactory(cell -> cell.getValue().userPasswordProperty());

            InitUser();
            tableUser.setItems(users);
        }catch(SQLException e){
            e.printStackTrace();
        }

        addAlternativeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String text = alternativeField.getText();
                Alternative alt = new Alternative(text);
                addNewAlternative(alt);
                tableAlternative.refresh();
            }
        });

        addUserButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String login = userLoginField.getText();
                String password = userPasswordField.getText();

                User user = new User(login, password);

                addNewUser(user);
                try{
                    idUser.setCellValueFactory(cell -> cell.getValue().idUserProperty());
                    loginUser.setCellValueFactory(cell -> cell.getValue().userLoginProperty());
                    passwordUser.setCellValueFactory(cell -> cell.getValue().userPasswordProperty());

                    InitUser();
                    tableUser.setItems(users);
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void addNewUser(User user) {
        String insert = "INSERT INTO " + Const.USER_TABLE+ "(" + Const.USER_USERNAME + ", "
                + Const.USER_PASSWORD + ")" + "VALUES(?,?)";

        try {
            String ConnectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName + "?autoReconnect=true&useSSL=false";
            Class.forName("com.mysql.jdbc.Driver");
            dbConnection = DriverManager.getConnection(ConnectionString, dbUser, dbPass);

            PreparedStatement prSt = dbConnection.prepareStatement(insert);
            prSt.setString(1, user.getUserLogin());
            prSt.setString(2, user.getUserPassword());
            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void InitUser() throws SQLException{
        try{
            String ConnectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName + "?autoReconnect=true&useSSL=false";
            Class.forName("com.mysql.jdbc.Driver");
            dbConnection = DriverManager.getConnection(ConnectionString, dbUser, dbPass);

            String select = "SELECT " + Const.USER_ID + ", " +
                    Const.USER_USERNAME + ", " + Const.USER_PASSWORD + " FROM " + Const.USER_TABLE;

            statement = dbConnection.createStatement();
            resultSet = statement.executeQuery(select);

            while(resultSet.next()){
                String id = resultSet.getString(Const.USER_ID);
                String login = resultSet.getString(Const.USER_USERNAME);
                String password = resultSet.getString(Const.USER_PASSWORD);
                users.add(new UserTV(id, login, password));
            }

        }catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
        finally {
            dbConnection.close();
            statement.close();
            resultSet.close();
        }
    }

    private void addNewAlternative(Alternative alt) {
        String insert = "INSERT INTO " + ConstAlternative.ALTERNATIVE_TABLE + "(" + ConstAlternative.ALTERNATIVE_TEXT
                + ")" + "VALUES(?)";

        try {
            String ConnectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName + "?autoReconnect=true&useSSL=false&characterEncoding=UTF-8";
            Class.forName("com.mysql.jdbc.Driver");
            dbConnection = DriverManager.getConnection(ConnectionString, dbUser, dbPass);

            PreparedStatement prSt = dbConnection.prepareStatement(insert);
            prSt.setString(1, alt.getText());
            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void PreInit() throws SQLException{
        try{
            String ConnectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName + "?autoReconnect=true&useSSL=false&characterEncoding=UTF-8";
            Class.forName("com.mysql.jdbc.Driver");
            dbConnection = DriverManager.getConnection(ConnectionString, dbUser, dbPass);

            String select = "SELECT " + ConstAlternative.ALTERNATIVE_ID + ", " +
                    ConstAlternative.ALTERNATIVE_TEXT + " FROM " + ConstAlternative.ALTERNATIVE_TABLE;

            statement = dbConnection.createStatement();
            resultSet = statement.executeQuery(select);

            while(resultSet.next()){
                String id = resultSet.getString(ConstAlternative.ALTERNATIVE_ID);
                String altText = resultSet.getString(ConstAlternative.ALTERNATIVE_TEXT);
                data.add(new Alternative(id, altText));
            }

        }catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
        finally {
            dbConnection.close();
            statement.close();
            resultSet.close();
        }
    }
}

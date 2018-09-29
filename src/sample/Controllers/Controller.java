package sample.Controllers;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.Admin.Admin;
import sample.Animation.Animation;
import sample.User.Database.Handler;
import sample.User.User;
import sample.protocol.Protocol;
import sample.protocol.ProtocolAdmin;

public class Controller implements Initializable{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField LoginField;

    @FXML
    private TextField AdminLoginField;

    @FXML
    private TextField NewLoginField;

    @FXML
    private PasswordField PasswordField;

    @FXML
    private PasswordField AdminPasswordField;

    @FXML
    private PasswordField NewPasswordField;

    @FXML
    private Button EnterButton;

    @FXML
    private Button AdminEnterButton;

    @FXML
    private Button SignUpButton;


    Socket socket;
    InputStream inputStream;
    OutputStream outputStream;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EnterButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                try{
                    socket = new Socket("localhost", 10000);

                    inputStream = socket.getInputStream();
                    outputStream = socket.getOutputStream();

                    String Login = LoginField.getText().trim();
                    String Password = PasswordField.getText().trim();

                    User user = new User();
                    user.setUserLogin(Login);
                    user.setUserPassword(Password);

                    byte[] data = Protocol.serialize(user);
                    try {
                        outputStream.write(data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                while (!socket.isClosed()) {
                                    User serverUser = Protocol.deserialize(inputStream);
                                    if (serverUser.getUserLogin().equals("true")){
                                        System.out.println("true");
                                        Platform.runLater(() -> openNewScene("/sample/view/programm.fxml", EnterButton));
                                    }
                                    else {
                                        System.out.println("false");
                                        Animation login = new Animation(LoginField);
                                        Animation password = new Animation(PasswordField);
                                        login.playAnim();
                                        password.playAnim();
                                        LoginField.clear();
                                        PasswordField.clear();
                                    }
                                }
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();

                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        });

        SignUpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try{
                    socket = new Socket("localhost", 5000);

                    inputStream = socket.getInputStream();
                    outputStream = socket.getOutputStream();

                    String Login = NewLoginField.getText().trim();
                    String Password = NewPasswordField.getText().trim();

                    User user = new User();
                    user.setUserLogin(Login);
                    user.setUserPassword(Password);

                    byte[] data = Protocol.serialize(user);
                    try {
                        outputStream.write(data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    NewLoginField.clear();
                    NewPasswordField.clear();
                    //Platform.runLater(() -> openNewScene("/sample/view/programm.fxml"));


                }catch(IOException e){
                    e.printStackTrace();
                }

            }
        });

        AdminEnterButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try{
                    socket = new Socket("localhost", 2000);

                    inputStream = socket.getInputStream();
                    outputStream = socket.getOutputStream();

                    String Login = AdminLoginField.getText().trim();
                    String Password = AdminPasswordField.getText().trim();

                    Admin admin = new Admin();
                    admin.setLogin(Login);
                    admin.setPassword(Password);

                    byte[] data = ProtocolAdmin.serializeAdmin(admin);
                    try {
                        outputStream.write(data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                while (!socket.isClosed()) {
                                    Admin serverAdmin = ProtocolAdmin.deserializeAdmin(inputStream);
                                    if (serverAdmin.getLogin().equals("true")){
                                        System.out.println("true");
                                        Platform.runLater(()->openNewScene("/sample/view/adminWindow.fxml", AdminEnterButton));
                                    }
                                    else {
                                        System.out.println("false");
                                        Animation login = new Animation(AdminLoginField);
                                        Animation password = new Animation(AdminPasswordField);
                                        login.playAnim();
                                        password.playAnim();
                                        AdminLoginField.clear();
                                        AdminPasswordField.clear();
                                    }
                                }
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();

                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        });

    }

    public void openNewScene(String window, Button button){
        button.getScene().getWindow().hide();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(window));

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }
}

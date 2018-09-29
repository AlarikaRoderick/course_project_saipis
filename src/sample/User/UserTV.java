package sample.User;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UserTV {
    StringProperty idUser;
    StringProperty userLogin;
    StringProperty userPassword;

    public UserTV(){}

    public UserTV(String idUser, String userLogin, String userPassword) {
        this.idUser = new SimpleStringProperty(idUser);
        this.userLogin = new SimpleStringProperty(userLogin);
        this.userPassword = new SimpleStringProperty(userPassword);
    }

    public UserTV(StringProperty idUser, StringProperty userLogin, StringProperty userPassword) {
        this.idUser = idUser;
        this.userLogin = userLogin;
        this.userPassword = userPassword;
    }

    public String getIdUser() {
        return idUser.get();
    }

    public StringProperty idUserProperty() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser.set(idUser);
    }

    public String getUserLogin() {
        return userLogin.get();
    }

    public StringProperty userLoginProperty() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin.set(userLogin);
    }

    public String getUserPassword() {
        return userPassword.get();
    }

    public StringProperty userPasswordProperty() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword.set(userPassword);
    }
}

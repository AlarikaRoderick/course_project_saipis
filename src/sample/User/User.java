package sample.User;

public class User {
    String UserLogin;
    String UserPassword;
    String Exist;

    public User(String userLogin, String userPassword) {
        UserLogin = userLogin;
        UserPassword = userPassword;
    }

    public User(){}

    public String getExist() {
        return Exist;
    }

    public void setExist(String exist) {
        Exist = exist;
    }

    public String getUserLogin() {
        return UserLogin;
    }

    public void setUserLogin(String userLogin) {
        UserLogin = userLogin;
    }

    public String getUserPassword() {
        return UserPassword;
    }

    public void setUserPassword(String userPassword) {
        UserPassword = userPassword;
    }
}

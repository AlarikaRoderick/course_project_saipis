package sample.Controllers;

import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.Animation.Animation;
import sample.Main;
import sample.User.Database.Configs;
import sample.alternative.Alternative;
import sample.alternative.Database.ConstAlternative;

public class ProgrammController extends Configs implements Initializable{

    private final ObservableList<Alternative> data = FXCollections.observableArrayList();
    private Connection dbConnection = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField AlterField1;

    @FXML
    private TextField AlterField2;

    @FXML
    private TextField AlterField3;

    @FXML
    private TextField Mark_1_1;

    @FXML
    private TextField Mark_2_1;

    @FXML
    private TextField Mark_3_1;

    @FXML
    private TextField Mark_4_1;

    @FXML
    private TextField Mark_1_2;

    @FXML
    private TextField Mark_2_2;

    @FXML
    private TextField Mark_3_2;

    @FXML
    private TextField Mark_4_2;

    @FXML
    private TextField Mark_1_3;

    @FXML
    private TextField Mark_2_3;

    @FXML
    private TextField Mark_3_3;

    @FXML
    private TextField Mark_4_3;

    @FXML
    private Button ButtonBack;

    @FXML
    private Button okButton;

    @FXML
    private Button clearButton;

    @FXML
    private TableView<Alternative> altTable;

    @FXML
    private TableColumn<Alternative, String> altColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try{
            altColumn.setCellValueFactory(cell -> cell.getValue().textAltProperty());
            PreInit();
            altTable.setItems(data);

        }catch(SQLException e){
            e.printStackTrace();
        }

        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                double mark11 = Double.parseDouble(Mark_1_1.getText());
                double revMark11 = 1 - mark11;

                double mark21 = Double.parseDouble(Mark_2_1.getText());
                double revMark21 = 1 - mark21;

                double mark31 = Double.parseDouble(Mark_3_1.getText());
                double revMark31 = 1 - mark31;

                double mark41 = Double.parseDouble(Mark_4_1.getText());
                double revMark41 = 1 - mark41;

                double mark12 = Double.parseDouble(Mark_1_2.getText());
                double revMark12 = 1 - mark12;

                double mark22 = Double.parseDouble(Mark_2_2.getText());
                double revMark22 = 1 - mark22;

                double mark32 = Double.parseDouble(Mark_3_2.getText());
                double revMark32 = 1 - mark32;

                double mark42 = Double.parseDouble(Mark_4_2.getText());
                double revMark42 = 1 - mark42;

                double mark13 = Double.parseDouble(Mark_1_3.getText());
                double revMark13 = 1 - mark13;

                double mark23 = Double.parseDouble(Mark_2_3.getText());
                double revMark23 = 1 - mark23;

                double mark33 = Double.parseDouble(Mark_3_3.getText());
                double revMark33 = 1 - mark33;

                double mark43 = Double.parseDouble(Mark_4_3.getText());
                double revMark43 = 1 - mark43;

                double amount1 = mark11+mark21+mark31+mark41;
                double amount2 = revMark11+revMark21+revMark31+revMark41;
                double amount3 = mark12+mark22+mark32+mark42;
                double amount4 = revMark12+revMark22+revMark32+revMark42;
                double amount5 = mark13+mark23+mark33+mark43;
                double amount6 = revMark13+revMark23+revMark33+revMark43;

                double f1 = amount1+amount3;
                double f2 = amount2+amount5;
                double f3 = amount4+amount6;

                double W1 = f1 / (f1+f2+f3);
                double W2 = f2 / (f1+f2+f3);
                double W3 = f3 / (f1+f2+f3);

                TreeMap<Double, String> treeMap = new TreeMap<Double, String>();
                treeMap.put(W1, AlterField1.getText());
                treeMap.put(W2, AlterField2.getText());
                treeMap.put(W3, AlterField3.getText());

                for(Map.Entry<Double, String> e: treeMap.entrySet()){
                    System.out.println(e.getKey()+ "" + e.getValue());
                }

                String alternative = null;

                if(W1>W2 && W1>W3) alternative = AlterField1.getText();
                if(W2>W1 && W2>W3) alternative = AlterField2.getText();
                if(W3>W1 && W3>W2) alternative = AlterField3.getText();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);

                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("Наиболее выгодная альтернатива: " + alternative);

                alert.showAndWait();
            }
        });

        clearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Mark_1_1.clear();
                Mark_1_2.clear();
                Mark_1_3.clear();
                Mark_2_1.clear();
                Mark_2_2.clear();
                Mark_2_3.clear();
                Mark_3_1.clear();
                Mark_3_2.clear();
                Mark_3_3.clear();
                Mark_4_1.clear();
                Mark_4_2.clear();
                Mark_4_3.clear();
                AlterField1.clear();
                AlterField2.clear();
                AlterField3.clear();
            }
        });

        ButtonBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ButtonBack.getScene().getWindow().hide();
                Stage stage = new Stage();
                Main main = new Main();
                try {
                    main.start(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void PreInit() throws SQLException{
        try{
            String ConnectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName + "?autoReconnect=true&useSSL=false";
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

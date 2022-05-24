package com.example.itmoprojectsmartwaiter;

import com.example.itmoprojectsmartwaiter.model.Meal;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

public class HistoryController implements Initializable {
    @FXML
    private TableView<Meal> table;
    @FXML private TableColumn<Meal, String> nameColumn;
    @FXML private TableColumn<Meal, Date> dateColumn;
    @FXML private TableColumn<Meal, Double> priceColumn;
    @FXML private TableColumn<Meal, Double> caloColumn;
    @FXML private TableColumn<Meal, Void> actionColumn;


    //Declare for BMR calculator
    @FXML private ToggleButton button_1;
    @FXML private ToggleGroup gender;
    @FXML private ToggleButton button_2;
    @FXML private TextField ageField;
    @FXML private TextField weightField;
    @FXML private TextField heightField;
    @FXML private ChoiceBox<String> typeChoice;
    private String[] activities = {
            "Do nothing, go nowhere",
            "Office work - study, not sports",
            "Office work - study, sport medium",
            "Moderate physical activity, not sports",
            "Moderate physical activity, moderate sport",
            "Moderate physical activity, heavy sports",
            "Heavy work - heavy sports",
            "Super heavy work"
    };
    @FXML private Label invalidLabel;
    @FXML private Label adviceLabel;


    @FXML private TextField caloDfField;
    @FXML private Label caloLastMonthLabel;
    @FXML private Label caloThisMonthLabel;


    //General declaration
    private ObservableList<Meal> mealList;
    static public double defaultCalo=0;
    private int checkGender=1;
    private Calendar cale = Calendar.getInstance();
    private Date date = cale.getTime();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mealList=IO.readFileMeal();
        caloLastMonthLabel.setText(String.valueOf(caloLastMonth()));
        caloThisMonthLabel.setText(String.valueOf(caloThisMonth()));


        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        caloColumn.setCellValueFactory(new PropertyValueFactory<>("calo"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button orderButton = new Button("delete");
            private final HBox pane = new HBox(orderButton);

            {
                orderButton.setOnAction(event -> {
                    Meal getPatient = getTableView().getItems().get(getIndex());
                    mealList.remove(getPatient);
                    caloLastMonthLabel.setText(String.valueOf(caloLastMonth()));
                    caloThisMonthLabel.setText(String.valueOf(caloThisMonth()));
                    IO.writeFileMeal(mealList);
                });

            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                setGraphic(empty ? null : pane);
            }
        });
        typeChoice.getItems().addAll(activities);
        typeChoice.setValue("Do nothing, go nowhere");
        table.setItems(mealList);

    }
    public void calculateBMR(){
        String invalid="";
        if(ageField.getText().equals("")||ageField.getText().chars().allMatch( Character::isAlphabetic )||Double.parseDouble(ageField.getText())<15 ||(Double.parseDouble(ageField.getText())>80)){
            invalid += " Invalid Age,";
        }
        if(weightField.getText().equals("")||weightField.getText().chars().allMatch( Character::isAlphabetic )||Double.parseDouble(weightField.getText())<0){
            invalid += " Invalid Weight,";
        }
        if(heightField.getText().equals("")||heightField.getText().chars().allMatch( Character::isAlphabetic )||Double.parseDouble(heightField.getText())<0){
            invalid += " Invalid Height,";
        }
        //invalid=invalid.substring(0,invalid.length()-1);
        invalidLabel.setText(invalid);
        if(!invalid.equals("")){
            invalid=invalid.substring(0,invalid.length()-1);
            invalidLabel.setText(invalid);
            return;
        }
        double resultBMR=0;
        double A = Double.parseDouble(ageField.getText());
        double W = Double.parseDouble(weightField.getText());
        double H = Double.parseDouble(heightField.getText());
        if(checkGender==1){
            resultBMR = 66.6+(13.75*W)+5*H+6.78*A;
        }else {
            resultBMR = 665+(9.56*W)+(1.85*H)+(4.68*A);
        }

        if(typeChoice.getValue().equals("Do nothing, go nowhere")){
            resultBMR=resultBMR*1.2;
        }else if(typeChoice.getValue().equals("Office work - study, not sports")){
            resultBMR=resultBMR*1.29;
        }else if(typeChoice.getValue().equals("Office work - study, sport medium")){
            resultBMR=resultBMR*1.375;
        }else if(typeChoice.getValue().equals("Moderate physical activity, not sports")){
            resultBMR=resultBMR*1.55;
        }else if(typeChoice.getValue().equals("Moderate physical activity, moderate sport")){
            resultBMR=resultBMR*1.63;
        }else if(typeChoice.getValue().equals("Moderate physical activity, heavy sports")){
            resultBMR=resultBMR*1.725;
        }else if(typeChoice.getValue().equals("Heavy work - heavy sports")){
            resultBMR=resultBMR*1.86;
        }else if(typeChoice.getValue().equals("Super heavy work")){
            resultBMR=resultBMR*2;
        }
        adviceLabel.setText("You need: "+Math.ceil(resultBMR* 100) / 100+" calories");
    }
    public double caloLastMonth(){
        int a =date.getMonth()-1;
        double rs=0.0;
        for(Meal me:mealList){
            if(me.getD().getMonth()==a){
                rs+=me.getCalo();
            }
        }
        return rs;
    }
    public double caloThisMonth(){
        int a =date.getMonth();
        double rs=0.0;
        for(Meal me:mealList){
            if(me.getD().getMonth()==a){
                rs+=me.getCalo();
            }
        }
        return rs;
    }
    public void setDfCalo(){
        defaultCalo=Double.parseDouble(caloDfField.getText());
        IO.writeFileMeal(mealList);
        caloDfField.setText("");
    }

    //handle toggleButton
    @FXML
    void toggleButton (ActionEvent event) {
        if (event.getSource() == button_1) {
            button_1.setStyle("-fx-background-color:#BDCEC6;-fx-background-radius: 30;");
            button_2.setStyle("-fx-background-color:#EFE9DB;-fx-background-radius: 30;");
            checkGender=1;

        }
        if (event.getSource() == button_2) {
            button_2.setStyle("-fx-background-color:#BDCEC6;-fx-background-radius: 30;");
            button_1.setStyle("-fx-background-color:#EFE9DB;-fx-background-radius: 30;");
            checkGender=2;
        }
    }

    //same as in homeController
    public void close(ActionEvent event) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    public void changeSceneHome(ActionEvent event) throws IOException {
        changeScene(event,"home-view.fxml");
    }
    public void changeSceneDishAdd(ActionEvent event) throws IOException {
        changeScene(event,"home-view-add.fxml");
    }
    public void changeScene(ActionEvent event,String nameF) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(nameF));
        Parent add = loader.load();
        Scene scene = new Scene(add);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
    }
}

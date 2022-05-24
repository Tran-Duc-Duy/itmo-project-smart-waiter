package com.example.itmoprojectsmartwaiter;

import com.example.itmoprojectsmartwaiter.model.Dish;
import com.example.itmoprojectsmartwaiter.model.Meal;
import com.example.itmoprojectsmartwaiter.model.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class HomeController implements Initializable {

    //General declaration
    @FXML
    private BorderPane bp;
    private ObservableList<Dish> dishList;
    private ObservableList<Dish> dishListSearch;
    private ObservableList<Order> dishListRecommend;
    private ObservableList<Order> dishListOrder;
    private List<Dish> soupList;
    private  List<Dish> saladList;
    private  List<Dish> mainList;
    private ObservableList<Meal> mealList;
    private double check;

    //Declare for gridview
    @FXML private GridPane dishGrid;


    //Declare for table recommend
    @FXML private Label recommendLabel;
    @FXML private TextField howManyField;
    @FXML private ChoiceBox<String> typeChoice;
    private String[] optionS ={"By price", "By calo", "By time"};
    @FXML private TableColumn<Order, Void> actionRecommendColumn;
    @FXML private TableView<Order> tableResult;
    @FXML private TableColumn<Order, String> resultColumn;
    @FXML private TableColumn<Order, String> sumColumn;


    //Declare for table meal
    @FXML private TableView<Order> tableOrder;
    @FXML private TableColumn<Order, String> orderColumn;
    @FXML private TableColumn<Order, Void> actionOrderColumn;
    @FXML private Label timeTTLabel;
    @FXML private Label caloTTLabel;
    @FXML private Label priceTTLabel;
    @FXML private Label caloDefaultLabel;
    @FXML private Label caloNeedLabel;


    // Declare the search box
    @FXML private TextField searchTextField;


    //General methods
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dishList = IO.readFile();
        mealList = IO.readFileMeal();
        caloDefaultLabel.setText(String.valueOf(historyController.defaultCalo));
        caloNeedLabel.setText(String.valueOf(caloToday()));
        //declare líst recommend
        dishListRecommend = FXCollections.observableArrayList();
        dishListOrder=FXCollections.observableArrayList();
        typeChoice.getItems().addAll(optionS);
        typeChoice.setValue("By price");
        actionRecommendColumn.setCellFactory(param -> new TableCell<>() {
            private final Button orderButton = new Button("order");
            private final HBox pane = new HBox(orderButton);

            {
                orderButton.setOnAction(event -> {
                    Order getPatient = getTableView().getItems().get(getIndex());
                    dishListOrder.add(getPatient);
                    Notification();
                });


            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                setGraphic(empty ? null : pane);
            }
        });

        //set value for column
        resultColumn.setCellValueFactory(new PropertyValueFactory<>("myOrder"));
        sumColumn.setCellValueFactory(new PropertyValueFactory<>("sum"));
        tableResult.setItems(dishListRecommend);
        //-----------
        searchTextField.setText("");
        //..............
        orderColumn.setCellValueFactory(new PropertyValueFactory<>("myOrder"));
        tableOrder.setItems(dishListOrder);
        //..................
        int temp= showListDish(dishList,0);
        //recommendLabel.setText("admin");
        //....

        //...set up button
        actionOrderColumn.setCellFactory(param -> new TableCell<Order, Void>() {
            private final Button deleteButton = new Button("delete");
            private final HBox pane = new HBox(deleteButton);

            {
                deleteButton.setOnAction(event -> {
                    Order getPatient = getTableView().getItems().get(getIndex());
                    dishListOrder.remove(getPatient);
                    Notification();
                });

            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                setGraphic(empty ? null : pane);
            }
        });

    }

    //method to calculate calories today
    public double caloToday(){
        //get today's date and calculate the total calories in the meal, then subtract the total from the default number of calories
        Calendar cale = Calendar.getInstance();
        Date date = cale.getTime();
        int a =date.getDate();
        double rs=0.0;
        for(Meal me:mealList){
            if(me.getD().getDate()==a){
                rs+=me.getCalo();
            }
        }
        return historyController.defaultCalo-rs;
    }

    //method to calculate total price, calories, time and then display
    private void Notification(){
        double ttTime=0.0, ttPrice=0.0,ttCalo=0.0;
        for(Order s:dishListOrder) {
            for(Dish d : s.getListDish()){
                ttTime+=d.getTime();
                ttCalo+=d.getTotalCalo();
                ttPrice+=d.getTotalPrice();
            }
        }
        //set values for labels
        timeTTLabel.setText(String.valueOf(ttTime));
        caloTTLabel.setText(String.valueOf(ttCalo));
        priceTTLabel.setText(String.valueOf(ttPrice));
    }

    //declare and display dishes on gridview
    private int showListDish(ObservableList<Dish> dishList, int columns){
        int row=1;
        try{
            //show each dish in turn
            for (int i=0;i<dishList.size();i++) {
                //each dish is displayed on an fxmlLoader
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("dish-item.fxml"));
                //initialize vbox and hbox
                VBox box = fxmlLoader.load();
                HBox hBox = new HBox();

                //initialize dish for view
                DishItemController dishItemController = fxmlLoader.getController();
                dishItemController.setDishItem(dishList.get(i));

                //declare two buttons
                Button btn= new Button("Order");
                btn.setPrefWidth(85);
                btn.setPrefHeight(20);
                btn.setStyle("-fx-font:Bold 18 Cambria; -fx-background-radius: 30;-fx-background-color:#D68162;-fx-background-insets: 5px;");
                List<Dish> re=new ArrayList();
                re.add(dishList.get(i));
                btn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        dishListOrder.add(new Order(re,1));
                        Notification();
                    }
                });

                Button btn1= new Button("Detail");
                btn1.setPrefWidth(85);
                btn1.setPrefHeight(20);
                btn1.setStyle("-fx-font:Bold 18 Cambria; -fx-background-radius: 30;-fx-background-color:#D68162;-fx-background-insets: 5px;");
                Dish getPatient = dishList.get(i);
                btn1.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("home-view-detail.fxml"));
                        Parent add = null;
                        try {
                            add = loader.load();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        Scene scene = new Scene(add);
                        DishDetailController detailController = loader.getController();
                        detailController.setDish(getPatient);
                        scene.setFill(Color.TRANSPARENT);
                        stage.setScene(scene);
                    }
                });

                //add buttons to hbox and hbox format
                hBox.getChildren().add(btn);
                hBox.getChildren().add(btn1);
                hBox.setStyle("-fx-background-insets: 10px;");
                hBox.setSpacing(10);
                hBox.setPadding(new Insets(0, 0, 0, 20));

                //add hbox to vbox
                box.getChildren().add(hBox);
                //add vbox to dishGrid and dishGrid format
                dishGrid.add(box, columns++, row);
                GridPane.setMargin(box, new Insets(30));
            }
        }catch (IOException e){
            e.printStackTrace ();
        }
        //support search function
        return columns;
    }
    //search dishes by input string from searchTextField
    public void searchBox(ActionEvent event){
        //Find the dish by name, if it is a valid dish, add it to the dishListSearch
        dishListSearch=FXCollections.observableArrayList();
        for (int i=0;i<dishList.size();i++) {
            if(dishList.get(i).getNameDish().contains(searchTextField.getText())){
                dishListSearch.add(dishList.get(i));
            }
        }
        //show list dishListSearch then show dishList form temp
        int temp= showListDish(dishListSearch,0);
        int temp2= showListDish(dishList,temp);
    }

    //Perform the calculation and display the area containing the recommend table
    public void randomSelection(ActionEvent event) {
        //get value from howManyField label
        check = Double.parseDouble(howManyField.getText());

        //divide the dish list into different lists by type
        soupList = new ArrayList<>();
        saladList = new ArrayList<>();
        mainList = new ArrayList<>();
        for (int i = 0; i < dishList.size(); i++) {
            if (dishList.get(i).getType().equals("soup")) {
                soupList.add(dishList.get(i));
            } else if (dishList.get(i).getType().equals("salad")) {
                saladList.add(dishList.get(i));
            } else {
                mainList.add(dishList.get(i));
            }
        }
        //show notification
        recommendLabel.setText(typeChoice.getValue()+": "+check);
        //call the function that does the calculation
        findTriplet(soupList,saladList,mainList,typeChoice.getValue(),check);
    }
    //support function for function randomSelection
    void findTriplet(List<Dish> soupList, List<Dish> saladList, List<Dish> mainList,
                     String byWhat,Double howMany)
    {
        //find three dishes whose sum is less than howMany
        List<Dish> oke;
        if(byWhat.equals("By price")) {
            dishListRecommend.removeAll(dishListRecommend);
            for (int i = 0; i < soupList.size(); i++) {
                for (int j = 0; j < saladList.size(); j++) {
                    for (int z = 0; z < mainList.size(); z++) {
                        if ((soupList.get(i).getTotalPrice() + saladList.get(j).getTotalPrice() + mainList.get(z).getTotalPrice()) < howMany) {
                            oke=new ArrayList<>();
                            oke.add(soupList.get(i));
                            oke.add(saladList.get(j));
                            oke.add(mainList.get(z));
                            dishListRecommend.add(new Order(oke,1));
                        }
                    }
                }
            }
            typeChoice.setValue("By price");
        }else if (byWhat.equals("By calo")){
            dishListRecommend.removeAll(dishListRecommend);
            for (int i = 0; i < soupList.size(); i++) {
                for (int j = 0; j < saladList.size(); j++) {
                    for (int z = 0; z < mainList.size(); z++) {
                        if ((soupList.get(i).getTotalCalo() + saladList.get(j).getTotalCalo() + mainList.get(z).getTotalCalo()) < howMany) {
                            oke=new ArrayList<>();
                            oke.add(soupList.get(i));
                            oke.add(saladList.get(j));
                            oke.add(mainList.get(z));
                            dishListRecommend.add(new Order(oke,2));
                        }
                    }
                }
            }
            typeChoice.setValue("By calo");
        }else{
            dishListRecommend.removeAll(dishListRecommend);
            for (int i = 0; i < soupList.size(); i++) {
                for (int j = 0; j < saladList.size(); j++) {
                    for (int z = 0; z < mainList.size(); z++) {
                        if ((soupList.get(i).getTime() + saladList.get(j).getTime() + mainList.get(z).getTime()) < howMany) {
                            oke=new ArrayList<>();
                            oke.add(soupList.get(i));
                            oke.add(saladList.get(j));
                            oke.add(mainList.get(z));
                            dishListRecommend.add(new Order(oke,3));
                        }
                    }
                }
            }
            typeChoice.setValue("By time");
        }
    }
    //function will run when confirm button is pressed
    public void confirmB(ActionEvent event) throws IOException {
        if(dishListOrder.size()==0){
            return;
        }
        //Read all past meals from file and get a list of meals
        ObservableList<Meal> mealList=IO.readFileMeal();

        //make a new Meal and add the meal to mealList then write it to the file
        Calendar cale = Calendar.getInstance();
        Date date = cale.getTime();
        Meal m = new Meal(dishListOrder,date);
        mealList.add(m);
        IO.writeFileMeal(mealList);

        //clean up dishListOrder
        dishListOrder.clear();
        //Notification();
        changeScene(event,"home-view.fxml");
    }
    //function to change scene
    public void changeScene(ActionEvent event, String nameF) throws IOException {
        //steps to get the stage, initialize and setting the loader, initialize the scene
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(nameF));
        Parent add = loader.load();
        Scene scene = new Scene(add);
        //setting the scene in the form TRANSPARENT
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
    }

    //function will run when the icon of the add button is pressed
    public void changeSceneDishAdd(ActionEvent event) throws IOException {
        changeScene(event,"home-view-add.fxml");
    }
    //function will run when the icon of the history button is pressed
    public void changeSceneDishHistory(ActionEvent event) throws IOException {
        changeScene(event,"home-view-history.fxml");
    }

    //close the window
    public void close(ActionEvent event) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}

package com.example.itmoprojectsmartwaiter;

import com.example.itmoprojectsmartwaiter.model.Dish;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class editController implements Initializable {
    @FXML
    private TableView<Dish> table;
    @FXML private TableColumn<Dish, Double> caloColumn;
    @FXML private TableColumn<Dish, Integer> idColumn;
    @FXML private TableColumn<Dish, String> imgColumn;
    @FXML private TableColumn<Dish, Double> moneyColumn;
    @FXML private TableColumn<Dish, String> nameColumn;
    @FXML private TableColumn<Dish, Double> timeColumn;
    @FXML private TableColumn<Dish, String> typeColumn;
    @FXML private TableColumn<Dish, String> descriptionColumn;
    @FXML private TableColumn<Dish, Void> actionColumn;

    //Declare for add dish
    @FXML private ImageView imageView;
    @FXML private TextField moneyText;
    @FXML private TextField caloText;
    @FXML private TextField timeText;
    @FXML private TextArea descriptionAText;
    @FXML private TextField nameText;
    @FXML private ChoiceBox<String> typeChoice;
    @FXML private Label invalidLabel;
    @FXML private Button btAdd;
    private String[] food ={"soup", "salad", "main course"};

    //General declaration
    @FXML private BorderPane bp;
    private ObservableList<Dish> dishList2;
    private String linkImage;
    private int flagADD=0;
    private int flagChooseFile=0;

    // Declare the search box
    @FXML private TextField keywordTextField;

    //initialize the content for the table
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dishList2 = IO.readFile();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nameDish"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        caloColumn.setCellValueFactory(new PropertyValueFactory<>("totalCalo"));
        moneyColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        imgColumn.setCellValueFactory(new PropertyValueFactory<>("imgView"));
        typeChoice.getItems().addAll(food);
        typeChoice.setValue("soup");
        actionColumn.setCellFactory(param -> new TableCell<Dish, Void>() {
            private final Button editButton = new Button("edit");
            private final Button deleteButton = new Button("delete");
            private final Button detailButton = new Button("detail");
            private final VBox pane = new VBox(deleteButton, editButton,detailButton );
            {
                deleteButton.setOnAction(event -> {
                    Dish getPatient = getTableView().getItems().get(getIndex());
                    int ids = getPatient.getId();
                    dishList2.remove(getPatient);
                    for (int i = ids - 1; i < dishList2.size(); i++) {
                        dishList2.get(i).setId(i + 1);
                    }
                    IO.writeFile(dishList2);
                });

                editButton.setOnAction(event -> {
                    Dish getPatient = getTableView().getItems().get(getIndex());
                    nameText.setText(getPatient.getNameDish());
                    moneyText.setText(String.valueOf(getPatient.getTotalPrice()));
                    caloText.setText(String.valueOf(getPatient.getTotalCalo()));
                    timeText.setText(String.valueOf(getPatient.getTime()));
                    btAdd.setText("Update");
                    linkImage=getPatient.getLinkImgString();
                    imageView.setImage(new Image(linkImage));
                    typeChoice.setValue(getPatient.getType());
                    descriptionAText.setText(getPatient.getDescription());
                    flagADD=getPatient.getId();
                    flagChooseFile=1;
                });
                detailButton.setOnAction(event -> {
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
                    Dish getPatient = getTableView().getItems().get(getIndex());
                    detailController.setDish(getPatient);
                    scene.setFill(Color.TRANSPARENT);
                    stage.setScene(scene);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                setGraphic(empty ? null : pane);
            }
        });
        table.setItems(dishList2);

        // Initial filtered list
        FilteredList<Dish> filteredData=new FilteredList<>(dishList2, b->true);
        keywordTextField.textProperty().addListener((observable,oldValue,newValue)->{
            filteredData.setPredicate(dish->{
                // If no search value then display all records or whatever records it current have.no changes.
                if(newValue.isEmpty()|| newValue.isBlank()|| newValue == null){
                    return true;
                }
                String searchKeyword=newValue.toLowerCase();
                if(dish.getNameDish().toLowerCase().contains(searchKeyword)){
                    return true;// Means we found a match in Product Name
                /*}else if(dish.getDescription().toLowerCase().indexOf(searchKeyword)>-1){
                    return true;// Means we found  a match in Description
                }else if(String.valueOf(dish.getTotalCalo()).indexOf(searchKeyword)>-1){
                    return true;
                }else if(dish.getType().indexOf(searchKeyword)>-1){
                    return true;
                }else if(String.valueOf(dish.getTotalTien()).indexOf(searchKeyword)>-1){
                    return true;
                }else if(String.valueOf(dish.getTime()).indexOf(searchKeyword)>-1){
                    return true;*/
                }else
                    return false;// no match found
            });
        });
        SortedList<Dish> sortedData=new SortedList<>(filteredData);
        // Bind sorted result with Table View
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        // Apply filtered and sorted data to the Table View
        table.setItems(sortedData);
    }

    //handle add button
    public void add(ActionEvent event) throws IOException {
        //check valid
        String invalid="";
        if(nameText.getText().equals("")){
            invalid +=" Invalid Name,";
        }
        if(moneyText.getText().equals("")||moneyText.getText().chars().allMatch( Character::isAlphabetic )||Double.parseDouble(moneyText.getText())<0){
            invalid += " Invalid Price,";
        }
        if(caloText.getText().equals("")||caloText.getText().chars().allMatch( Character::isAlphabetic )||Double.parseDouble(caloText.getText())<0){
            invalid += " Invalid Calo,";
        }
        if(timeText.getText().equals("")||timeText.getText().chars().allMatch( Character::isAlphabetic )||Double.parseDouble(timeText.getText())<0){
            invalid += " Invalid Time,";
        }
        if(flagChooseFile==0){
            invalid += " Pls choose image,";
        }
        invalidLabel.setText(invalid);
        if(!invalid.equals("")){
            invalid=invalid.substring(0,invalid.length()-1);
            invalidLabel.setText(invalid);
            return;
        }

        //initialize a dish
        Dish newDish = new Dish();
        newDish.setId(dishList2.size()+1);

        newDish.setNameDish(nameText.getText());
        newDish.setTotalPrice(Double.parseDouble(moneyText.getText()));
        newDish.setTotalCalo(Double.parseDouble(caloText.getText()));
        newDish.setTime(Double.parseDouble(timeText.getText()));
        newDish.setDescription(descriptionAText.getText());
        linkImage=linkImage.replace("file:/","");
        linkImage=linkImage.replaceAll("/","\\\\");
        linkImage=linkImage.replaceAll("%20"," ");
        linkImage = IO.copyImg(linkImage,nameText.getText());
        newDish.setLinkImgString(linkImage);
        newDish.setImgView(new ImageView(imageView.getImage()));
        newDish.setType(typeChoice.getValue());
        for(Dish ma:dishList2){
            if(ma.getNameDish().compareToIgnoreCase(newDish.getNameDish())==0&&flagADD==0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Result: New dish is "+newDish.getNameDish()+". This dish already exists!");
                alert.setContentText("Cannot contain two dishes with the same name");
                alert.showAndWait();
                return;
            }
        }
        //used when mode is "update"
        if(flagADD!=0){
            newDish.setId(flagADD);
            for(Dish dis:dishList2){
                if(dis.getId()==flagADD){
                    dis.setNameDish(newDish.getNameDish());
                    dis.setType(newDish.getType());
                    dis.setDescription(newDish.getDescription());
                    dis.setTime(newDish.getTime());
                    dis.setTotalCalo(newDish.getTotalCalo());
                    dis.setTotalPrice(newDish.getTotalPrice());
                    dis.setLinkImgString(newDish.getLinkImgString());
                    dis.setImgView(newDish.getImgView());
                }
            }
        }
        else{
            dishList2.add(newDish);
        }

        IO.writeFile(dishList2);
        flagADD=0;
        flagChooseFile=0;
        changeScene(event,"home-view-add.fxml");
    }
    public void chooseFile(ActionEvent event){
        Stage stage = (Stage) bp.getScene().getWindow();
        //create a variable to select the file
        FileChooser fc =new FileChooser();
        fc.setTitle("Choose a image");
        //only accept files with the extension:"*.jpg","*.png"
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files","*.jpg","*.png");
        fc.getExtensionFilters().add(imageFilter);
        File file = fc.showOpenDialog(stage);
        if(file !=null){
            linkImage=file.toURI().toString();
            Image image =new Image(linkImage,200,200,true,true);
            imageView.setImage(image);
            flagChooseFile=1;
        }
    }

    //same as in homeController
    public void close(ActionEvent event) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    public void changeSceneDishHistory(ActionEvent event) throws IOException {
        changeScene(event,"home-view-history.fxml");
    }
    public void changeSceneHome(ActionEvent event) throws IOException {
        changeScene(event,"home-view.fxml");
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

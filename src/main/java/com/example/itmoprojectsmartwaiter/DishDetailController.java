package com.example.itmoprojectsmartwaiter;

import com.example.itmoprojectsmartwaiter.model.Dish;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class DishDetailController {
    @FXML
    private Label caloLabel;

    @FXML
    private Label giaLabel;

    @FXML
    private Label nameLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label typeLabel;
    @FXML
    private ImageView img;
    @FXML
    private Label descriptionLabel;

    public void setDish(Dish dish){
        caloLabel.setText(String.valueOf(dish.getTotalCalo()));
        giaLabel.setText(String.valueOf(dish.getTotalPrice()));
        nameLabel.setText(dish.getNameDish());
        typeLabel.setText(dish.getType());
        timeLabel.setText(dish.getTime()+"m");
        img.setImage(dish.getImgView().getImage());
        descriptionLabel.setText(dish.getDescription());
    }
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

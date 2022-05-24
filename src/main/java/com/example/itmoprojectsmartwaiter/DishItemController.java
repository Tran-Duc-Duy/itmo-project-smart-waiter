package com.example.itmoprojectsmartwaiter;

import com.example.itmoprojectsmartwaiter.model.Dish;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class DishItemController {
    @FXML
    private Label caloLabel;
    @FXML private Label giaLabel;
    @FXML private Label nameLabel;
    @FXML private Label timeLabel;
    @FXML private Label typeLabel;
    @FXML private ImageView img;
    @FXML private Label descriptionLabel;
    public void setDishItem(Dish dish){
        caloLabel.setText(String.valueOf(dish.getTotalCalo()));
        giaLabel.setText(String.valueOf(dish.getTotalPrice()));
        nameLabel.setText(dish.getNameDish());
        typeLabel.setText(dish.getType());
        timeLabel.setText(dish.getTime()+"m");
        img.setImage(dish.getImgView().getImage());
        descriptionLabel.setText(dish.getDescription());
    }
}

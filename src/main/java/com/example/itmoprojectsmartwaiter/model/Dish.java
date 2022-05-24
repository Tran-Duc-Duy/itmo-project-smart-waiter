package com.example.itmoprojectsmartwaiter.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Dish {
    private int id;
    private String nameDish;
    private String type;
    private double totalCalo;
    private double totalPrice;
    private double time;
    private ImageView imgView;
    private String linkImgString;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Dish(int id, String nameDish, String type, double totalCalo, double totalPrice, double time, String linkImgString, String description) {
        this.id = id;
        this.nameDish = nameDish;
        this.type = type;
        this.totalCalo = totalCalo;
        this.totalPrice = totalPrice;
        this.time = time;
        this.imgView = new ImageView(new Image(linkImgString));
        this.linkImgString = linkImgString;
        this.description =description;
    }

    public Dish() {
    }
    @Override
    public String toString() {
        //String linkImgStringTemp=linkImgString.substring(linkImgString.indexOf("src"),linkImgString.length()-1);
        return id+";"+nameDish+";"+type+";"+totalCalo+";"+ totalPrice +";"+time+";"+linkImgString+";"+costumDescription(description)+";";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameDish() {
        return nameDish;
    }

    public void setNameDish(String nameDish) {
        this.nameDish = nameDish;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getTotalCalo() {
        return totalCalo;
    }

    public void setTotalCalo(double totalCalo) {
        this.totalCalo = totalCalo;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public ImageView getImgView() {
        return imgView;
    }

    public void setImgView(ImageView imgView) {
        this.imgView = imgView;
    }

    public String getLinkImgString() {
        return linkImgString;
    }

    public void setLinkImgString(String linkImgString) {
        this.linkImgString = linkImgString;
    }
    private String costumDescription(String des){
        return des.replaceAll("\\n","/n");
    }
    public void costumDescription2(){
        this.description=description.replaceAll("/n","\n");
    }

}


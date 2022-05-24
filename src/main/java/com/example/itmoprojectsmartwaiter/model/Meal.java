package com.example.itmoprojectsmartwaiter.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Meal {
    private List<Order> listOrder = new ArrayList<Order>();
    private String name ="";
    private Date date;
    private double price;
    private double calo;

    SimpleDateFormat formatter1=new SimpleDateFormat("dd:MM:yyyy");
    public Meal(List<Order> listOrder, Date date) {
        this.listOrder = listOrder;
        this.date = date;
    }
    public Meal(List<Order> listOrder, Date date, double price, double calo) {
        this.listOrder = listOrder;
        this.date = date;
        this.price = price;
        this.calo = calo;
        this.name = this.toString();
    }
    @Override
    public String toString() {

        String str = formatter1.format(date)+ "#";
        for (Order order : listOrder) {
            for(Dish item : order.getListDish()){
                str += item.toString() + "@";
            }
            str+= "#";
        }
        return str;
    }
    public String getName() {
        String name="";
        for(Order o : listOrder){
            name+=o.toString()+"\n";
        }
        return name;
    }
    public List<Order> getListOrder() {
        return listOrder;
    }

    public void setListOrder(List<Order> listOrder) {
        this.listOrder = listOrder;
    }

    public String getDate() {
        return formatter1.format(date);
    }
    public Date getD(){
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getPrice() {
        double a=0;
        for(Order o : listOrder){
            for(Dish d:o.getListDish()){
                a+=d.getTotalPrice();
            }
        }
        return a;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getCalo() {
        double a=0;
        for(Order o : listOrder){
            for(Dish d:o.getListDish()){
                a+=d.getTotalCalo();
            }
        }
        return a;
    }

    public void setCalo(double calo) {
        this.calo = calo;
    }


}
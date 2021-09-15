package com.springapi.springrestapi.model;

public class Car {
    private String carColor;
    private String carNumber;

    public Car(String carColor, String carNumber){
        this.carColor = carColor;
        this.carNumber = carNumber;
    }
    public String getCarNumber() {
        return carNumber;
    }

    public String getCarColor() {
        return  this.carColor;
    }
    
}  

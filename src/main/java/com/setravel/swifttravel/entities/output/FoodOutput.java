package com.setravel.swifttravel.entities.output;

import java.math.BigDecimal;
import java.util.Base64;

import com.setravel.swifttravel.entities.Food;

import lombok.Data;

@Data
public class FoodOutput {
    private String id;
    private String trainsId;
    private String foodName;
    private String foodType;
    private BigDecimal price;
    private Boolean lunch;
    private Boolean dinner;
    private String picUrl;
    private Boolean del;
    private Integer number;

    public static FoodOutput from(Food food) {
        FoodOutput foodOutput = new FoodOutput();
        foodOutput.setId(Base64.getEncoder().encodeToString(food.getId()));
        foodOutput.setTrainsId(Base64.getEncoder().encodeToString(food.getTrainsId()));
        foodOutput.setFoodName(food.getFoodName());
        foodOutput.setFoodType(food.getFoodType());
        foodOutput.setPrice(food.getPrice());
        foodOutput.setLunch(food.getLunch());
        foodOutput.setDinner(food.getDinner());
        foodOutput.setPicUrl(food.getPicUrl());
        foodOutput.setDel(food.getDel());
        foodOutput.setNumber(food.getNumber());
        return foodOutput;
    }
}

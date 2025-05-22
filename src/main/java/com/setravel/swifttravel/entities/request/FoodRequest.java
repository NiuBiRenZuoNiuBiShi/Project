package com.setravel.swifttravel.entities.request;

import java.math.BigDecimal;
import java.util.Base64;

import com.setravel.swifttravel.entities.Food;

import lombok.Data;

@Data
public class FoodRequest {
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

    public Food toEntity() {
        Food food = new Food();
        food.setId(Base64.getDecoder().decode(id));
        food.setTrainsId(Base64.getDecoder().decode(trainsId));
        food.setFoodName(foodName);
        food.setFoodType(foodType);
        food.setPrice(price);
        food.setLunch(lunch);
        food.setDinner(dinner);
        food.setPicUrl(picUrl);
        food.setDel(del);
        food.setNumber(number);
        return food;
    }
}

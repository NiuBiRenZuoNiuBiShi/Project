package com.setravel.swifttravel.service;

import java.util.List;

import com.setravel.swifttravel.entities.Result;
import com.setravel.swifttravel.entities.request.FoodRequest;

public interface FoodService {
    Result addFoodList(List<FoodRequest> foodList);

    Result addFood(FoodRequest foodRequest);

    Result deleteFoods(List<String> foodIds);

    Result getFoodListByIds(List<String> foodIds);

    Result getFoodListByTrainId(String trainId);


    Result buyFoodList(List<FoodRequest> requestList);
}

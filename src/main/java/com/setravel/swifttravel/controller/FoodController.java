package com.setravel.swifttravel.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.setravel.swifttravel.entities.Result;
import com.setravel.swifttravel.entities.request.FoodRequest;
import com.setravel.swifttravel.service.FoodService;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * Rest controller for handling food-related operations in the Swift Travel application.
 * Provides endpoints for adding, deleting, retrieving, and purchasing food items.
 * <p>
 * The controller interfaces with {@link FoodService} to process all food-related requests.
 */
@RestController
public class FoodController {
    
    /**
     * The food service used to handle food operations.
     */
    @Resource
    private FoodService foodService;

    /**
     * Adds a list of food items.
     * 
     * @param foodList List of food requests to be added
     * @return Result containing the operation status and data
     */
    @PostMapping("/api/food/adds")
    public Result addFoodList(@RequestBody List<FoodRequest> foodList) {
        return foodService.addFoodList(foodList);
    }
    
    /**
     * Adds a single food item.
     * 
     * @param foodRequest The food request containing the details of the food item
     * @return Result containing the operation status and data
     */
    @PostMapping("/api/food/add")
    public Result addFood(@RequestBody FoodRequest foodRequest) {
        return foodService.addFood(foodRequest);
    }

    /**
     * Deletes multiple food items by their IDs.
     * 
     * @param foodIds List of food IDs to be deleted
     * @return Result containing the operation status and data
     */
    @PostMapping("/api/food/delete")
    public Result deleteFoods(@RequestBody List<String> foodIds) {
        return foodService.deleteFoods(foodIds);
    }

    /**
     * Retrieves food items by their IDs.
     * 
     * @param foodIds List of food IDs to query
     * @return Result containing the operation status and the retrieved food items
     */
    @PostMapping("/api/food/getByIds")
    public Result getFoodListByIds(@RequestBody List<String> foodIds) {
        return foodService.getFoodListByIds(foodIds);
    }

    /**
     * Retrieves all food items available for a specific train.
     * 
     * @param trainId ID of the train to get the food items for
     * @return Result containing the operation status and the retrieved food items
     */
    @PostMapping("/api/food/getByTrainId")
    public Result getFoodListByTrainId(@RequestBody byte[] trainId) {
        return foodService.getFoodListByTrainId(trainId);
    }

    /**
     * Processes the purchase of a list of food items.
     * 
     * @param requestList List of food requests to be purchased
     * @return Result containing the operation status and purchase details
     */
    @PostMapping("/api/food/buy")
    public Result buyFoodList(@RequestBody List<FoodRequest> requestList) {
        return foodService.buyFoodList(requestList);
    }
}

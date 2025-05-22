package com.setravel.swifttravel.service.impl;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.setravel.swifttravel.entities.Food;
import com.setravel.swifttravel.entities.Result;
import com.setravel.swifttravel.entities.Users;
import com.setravel.swifttravel.entities.output.FoodOutput;
import com.setravel.swifttravel.entities.request.FoodRequest;
import com.setravel.swifttravel.mapper.FoodMapper;
import com.setravel.swifttravel.mapper.UserMapper;
import com.setravel.swifttravel.security.UserContext;
import com.setravel.swifttravel.service.FoodService;
import com.setravel.swifttravel.utils.UUIDUtil;

import jakarta.annotation.Resource;

@Service
public class FoodServiceImpl implements FoodService {

    @Resource
    private FoodMapper foodMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public Result addFoodList(List<FoodRequest> requestsList) {
        List<Food> foodList = requestsList.stream().map(FoodRequest::toEntity).map(food -> {
            return food.setId(UUIDUtil.generateUUIDBytes());
        }).collect(Collectors.toList());

        try {
            foodMapper.insert(foodList);
            return Result.success();
        } catch (Exception e) {
            return Result.error("添加失败");
        }
    }

    @Override
    public Result addFood(FoodRequest foodRequest) {
        Food food = foodRequest.toEntity().setId(UUIDUtil.generateUUIDBytes());
        try {
            foodMapper.insert(food);
            return Result.success();
        } catch (Exception e) {
            return Result.error("添加失败");
        }
    }

    @Override
    public Result deleteFoods(List<String> foodIds) {
        List<byte[]> foodIdsBinary = foodIds.stream().map(Base64.getDecoder()::decode).collect(Collectors.toList());
        try {
            foodMapper.deleteByIds(foodIdsBinary);
            return Result.success();
        } catch (Exception e) {
            return Result.error("删除失败");
        }
    }

    @Override
    public Result getFoodListByIds(List<String> foodIds) {
        List<byte[]> foodIdsBinary = foodIds.stream().map(Base64.getDecoder()::decode).collect(Collectors.toList());
        try {
            List<Food> foodList = foodMapper.selectByIds(foodIdsBinary);
            List<FoodOutput> foodOutputList = foodList.stream().map(FoodOutput::from).collect(Collectors.toList());
            return Result.success(foodOutputList);
        } catch (Exception e) {
            return Result.error("查询失败");
        }
    }

    @Override
    public Result getFoodListByTrainId(String trainId) {
        byte[] trainIdBinary = Base64.getDecoder().decode(trainId);
        try {
            List<Food> foodList = foodMapper.selectList(
                    new LambdaQueryWrapper<Food>().eq(Food::getTrainsId, trainIdBinary).eq(Food::getDel, false));
            List<FoodOutput> foodOutputList = foodList.stream().map(FoodOutput::from).collect(Collectors.toList());
            return Result.success(foodOutputList);
        } catch (Exception e) {
            return Result.error("查询失败");
        }
    }

    @Override
    public Result buyFoodList(List<FoodRequest> requestList) {
        List<Food> foodList = requestList.stream().map(FoodRequest::toEntity).collect(Collectors.toList());
        Users currentUser = UserContext.getCurrentUser(userMapper);
        // TODO: Implement the logic to buy food
        throw new UnsupportedOperationException("Not implemented yet");
    }

}

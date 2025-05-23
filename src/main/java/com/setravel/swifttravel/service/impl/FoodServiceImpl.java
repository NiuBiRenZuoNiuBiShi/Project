package com.setravel.swifttravel.service.impl;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.activerecord.AbstractModel;
import com.setravel.swifttravel.entities.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
    public Result getFoodListByTrainId(byte[] trainId) {
        byte[] trainIdBinary = trainId;
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
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Result buyFoodList(List<FoodRequest> requestList) {
        List<Food> foodList = requestList.stream().map(FoodRequest::toEntity).toList();

        // 使用悲观锁 + 库存检查
        try {
            // 先锁定相关食物记录，防止并发修改
            List<Food> lockedFoods = foodMapper.selectFoodsForUpdate(foodList.stream().map(Food::getId).toList());

            // 检查库存是否充足
            Map<byte[], Integer> requestMap = foodList.stream().collect(Collectors.toMap(Food::getId, Food::getNumber));

            for (Food lockedFood : lockedFoods) {
                Integer requestNum = requestMap.get(lockedFood.getId());
                if (requestNum == null || lockedFood.getNumber() < requestNum) {
                    return Result.error("库存不足: " + lockedFood.getFoodName());
                }
            }

            // 扣减库存
            int affectedRows = foodMapper.decreaseFoodsWithCheck(foodList);
            if (affectedRows != foodList.size()) {
                throw new RuntimeException("库存扣减失败，可能库存不足");
            }

        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.error("购买失败: " + e.getMessage());
        }

        // 创建订单
        Users currentUser = UserContext.getCurrentUser(userMapper);
        FoodOrders foodOrder = new FoodOrders();
        foodOrder.setId(UUIDUtil.generateUUIDBytes());
        foodOrder.setUserId(currentUser.getId());
        foodOrder.setDel(false);
        foodOrder.setPayId(null);

        List<FoodOrderItems> foodOrderItemsList = foodList.stream().map(food -> {
            FoodOrderItems foodOrderItem = new FoodOrderItems();
            foodOrderItem.setId(UUIDUtil.generateUUIDBytes());
            foodOrderItem.setFoodId(food.getId());
            foodOrderItem.setFoodNumber(food.getNumber());
            foodOrderItem.setOrderId(foodOrder.getId());
            return foodOrderItem;
        }).toList();

        // 插入订单数据
        foodOrder.insert();
        foodOrderItemsList.forEach(AbstractModel::insert);

        return Result.success(foodOrder);
    }
}

package com.setravel.swifttravel.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.setravel.swifttravel.entities.Hotel;
import com.setravel.swifttravel.entities.Reservation;
import com.setravel.swifttravel.entities.Room;
import com.setravel.swifttravel.entities.output.HotelReserveOutput;
import com.setravel.swifttravel.entities.request.HotelReserveRequest;
import com.setravel.swifttravel.mapper.HotelMapper;
import com.setravel.swifttravel.mapper.ReservationMapper;
import com.setravel.swifttravel.mapper.RoomMapper;
import com.setravel.swifttravel.service.ReservationService;
import com.setravel.swifttravel.utils.UUIDUtil;

import jakarta.annotation.Resource;

@Service
public class ReservationServiceImpl extends ServiceImpl<ReservationMapper, Reservation> implements ReservationService{

    @Resource
    private RoomMapper roomMapper;

    @Resource
    private HotelMapper hotelMapper;

    @Override
    @Transactional
    public HotelReserveOutput createReservation(HotelReserveRequest request) {
        // ID转换与参数校验
        byte[] userIdBytes = UUIDUtil.StringToBytes(request.getUserId());
        byte[] hotelIDBytes = UUIDUtil.StringToBytes(request.getHotelId());
        // 排除输入为空且入住日期在退房日期之前的情况
        if (userIdBytes == null || hotelIDBytes == null || request.getRoomType() == null ||
            request.getCheckinDate() == null || request.getCheckoutDate() == null || 
            request.getCheckinDate().isAfter(request.getCheckoutDate()) || request.getCheckoutDate().isEqual(request.getCheckinDate())) {
                throw new IllegalArgumentException("无效的预订请求参数");
        }

        Hotel hotel = hotelMapper.selectOne(new LambdaQueryWrapper<Hotel>()
                    .eq(Hotel::getId, hotelIDBytes)
                    .eq(Hotel::getDel, false));
        if (hotel == null) {
            throw new IllegalArgumentException("酒店不存在");
        }

        long nights = ChronoUnit.DAYS.between(request.getCheckinDate(), request.getCheckoutDate());

        // 检查指定房型在整个日期范围内的可用性
        List<Room> dailyRoomRecords = roomMapper.findAvailableRooms(hotelIDBytes, request.getCheckinDate(), request.getCheckoutDate(), request.getNumberofPeople());
        // 按日期分组，并且只选取特定房型和当前可用的房间
        Map<LocalDate, List<Room>> roomsByDateForType = dailyRoomRecords.stream()
            // 只选取当前可用的房间
            .filter(room -> request.getRoomType().equals(room.getRoomType()) && Boolean.TRUE.equals(room.getAvailability()))
            // 
            .collect(Collectors.groupingBy(Room::getRoomDate));
        // 存储将要被预订的每日房间记录
        List<Room> roomsToReserve = new ArrayList<>();
        for (int i = 0; i < nights; i++) {
            LocalDate currDate = request.getCheckinDate().plusDays(i);
            List<Room> availableRoomsOnDate = roomsByDateForType.get(currDate);
            if (CollectionUtils.isEmpty(availableRoomsOnDate)) {
                // 如果没有任何一天符合条件的可用房间，则库存不足
                throw new RuntimeException("所选房型在 " + currDate + " 库存不足。");
            }
            // 默认选择该日期下该房型的第一个可用记录作为预订目标
            roomsToReserve.add(availableRoomsOnDate.get(0));
        }

        if (roomsToReserve.size() < nights) {
            throw new RuntimeException("未能为所有预订日期找到足够库存的房型 ["+ request.getRoomType() +"]。");
        }

        // 创建 Reservation 实体并生成ID
        Reservation reservation = new Reservation();
        byte[] rsvId = UUIDUtil.generateUUIDBytes();
        reservation.setId(rsvId);
        reservation.setUserId(userIdBytes);
        reservation.setHotelId(hotelIDBytes);
        reservation.setRoomId(roomsToReserve.get(0).getId()); //只取第一个(还需要考虑一下)
        reservation.setCheckinDate(request.getCheckinDate());
        reservation.setCheckoutDate(request.getCheckoutDate());
        reservation.setStatus("CONFIRMED");
        reservation.setBookingTime(LocalDateTime.now());
        reservation.setDel(false);
        
        // 更新Room表中的availability状态和current_reservation_id，并使用乐观锁
        for (Room roomToUpd : roomsToReserve) {
            roomToUpd.setAvailability(false); // 标记为不可用
            roomToUpd.setCurrent_reservation_id(rsvId); // 关联到当前订单id

            int updateRows = roomMapper.updateById(roomToUpd); // mybatis-plus会自动处理version
            if (updateRows == 0) {
                // 更新失败，说明version不匹配，发生并发冲突
                throw new OptimisticLockingFailureException("房间 [" + roomToUpd.getRoomType() + 
                                                            "] 在日期 [" + roomToUpd.getRoomDate() + 
                                                            "] (ID: " + UUIDUtil.bytesToString(roomToUpd.getId()) +
                                                            ") 的库存信息已被其他用户修改，请重试。");
            }
        }

        this.save(reservation);
        return convertToReserveOutput(reservation, hotel.getName(), request.getRoomType());
    }

    @Override
    public IPage<HotelReserveOutput> getUserReservations(String userIdString, int pageNum, int pageSize) {
        byte[] userIdBytes = UUIDUtil.StringToBytes(userIdString);
        if (userIdBytes == null) return null;

        Page<Reservation> page = new Page<>(pageNum, pageSize);
        IPage<Reservation> reservationPage = this.page(page,
                new LambdaQueryWrapper<Reservation>()
                        .eq(Reservation::getUserId, userIdBytes)
                        .eq(Reservation::getDel, false)
                        .orderByDesc(Reservation::getBookingTime)
        );

        return reservationPage.convert(res -> {
            Hotel hotel = hotelMapper.selectOne(new LambdaQueryWrapper<Hotel>().eq(Hotel::getId, res.getHotelId()));
            String hotelName = (hotel != null) ? hotel.getName() : "未知酒店";
            // 通过roomId找到roomType
            String roomType = "未知房型";
            if(res.getRoomId() != null){
                Room room = roomMapper.selectById(res.getRoomId());
                if(room != null) roomType = room.getRoomType();
            }
            return convertToReserveOutput(res, hotelName, roomType);
        });
    }

    @Override
    // 有可能用不到？可能放在前面就展示完了
    public HotelReserveOutput getReservationDetails(String rsvIdString, String userIdString) {
        byte[] reservationIdBytes = UUIDUtil.StringToBytes(rsvIdString);
        byte[] userIdBytes = UUIDUtil.StringToBytes(userIdString);
        if (reservationIdBytes == null || userIdBytes == null) throw new IllegalArgumentException("无效的ID");
        
        // 根据提供的用户和订单号找到具体的订单
        Reservation reservation = this.getOne(
            new LambdaQueryWrapper<Reservation>()
                .eq(Reservation::getId, reservationIdBytes)
                .eq(Reservation::getUserId, userIdBytes) // 校验用户
                .eq(Reservation::getDel, false)
        );
        if (reservation == null) return null;

        Hotel hotel = hotelMapper.selectOne(new LambdaQueryWrapper<Hotel>().eq(Hotel::getId, reservation.getHotelId()));
        String hotelName = (hotel != null) ? hotel.getName() : "未知酒店";
        String roomType = "未知房型";
        if(reservation.getRoomId() != null){
            Room room = roomMapper.selectById(reservation.getRoomId());
            if(room != null) roomType = room.getRoomType();
        }
        return convertToReserveOutput(reservation, hotelName, roomType);
    
    }

    @Override
    @Transactional
    public boolean cancelReservation(String rsvIdString, String userIdString) {
        byte[] reservationIdBytes = UUIDUtil.StringToBytes(rsvIdString);
        byte[] userIdBytes = UUIDUtil.StringToBytes(userIdString);

        if (reservationIdBytes == null || userIdBytes == null) {
            throw new IllegalArgumentException("无效的订单ID或用户ID。");
        }

        // 找到要取消的那个预订
        Reservation reservation = this.getOne(new LambdaQueryWrapper<Reservation>()
                .eq(Reservation::getId, reservationIdBytes)
                .eq(Reservation::getUserId, userIdBytes) // 确保是用户自己的订单
                .eq(Reservation::getDel, false));

        if (reservation == null) {
            throw new RuntimeException("预订不存在或已被取消。");
        }

        // 假设只有CONFIRMED状态可以取消
        if (!"CONFIRMED".equals(reservation.getStatus())) { 
            throw new RuntimeException("当前订单状态 (" + reservation.getStatus() + ") 不允许取消。");
        }

        // 更新预订状态
        reservation.setStatus("CANCELLED");
        reservation.setDel(true); // 逻辑删除
        boolean rsvUpdSuccess = this.updateById(reservation);

        if (rsvUpdSuccess) {
            // 恢复Room表中的availability状态和清除current_reservation_id (使用乐观锁)
            // 通过 current_reservation_id 来精确查找所有属于此预订的每日房间记录
            List<Room> bookedRooms = roomMapper.selectList(
                new LambdaQueryWrapper<Room>().eq(Room::getCurrent_reservation_id, reservation.getId())
            );

            if (CollectionUtils.isEmpty(bookedRooms) && reservation.getCheckinDate() != null && reservation.getCheckoutDate() != null &&
                ChronoUnit.DAYS.between(reservation.getCheckinDate(), reservation.getCheckoutDate()) > 0) {
                // 如果预订时长大于0但没有找到关联的房间记录，这可能是一个数据不一致的信号
                //Logger.warn("预订ID {} (用户ID {}) 已取消，但未找到任何通过current_reservation_id关联的房间记录来恢复库存。", rsvIdString, userIdString);
                // 预订本身已取消，但库存恢复可能需要人工检查。
            }

            for (Room roomToRestore : bookedRooms) {
                roomToRestore.setAvailability(true);
                roomToRestore.setCurrent_reservation_id(null);
                
                int updateRows = roomMapper.updateById(roomToRestore);
                if (updateRows == 0) {
                    // throw exception? 恢复房间库存时发生乐观锁冲突或记录已更改
                }
            }
        }

        return rsvUpdSuccess;
    }
    
    private HotelReserveOutput convertToReserveOutput(Reservation reservation, String hotelName, String roomType) {
        if (reservation == null) return null;
        HotelReserveOutput hro = new HotelReserveOutput();
        hro.setId(UUIDUtil.bytesToString(reservation.getId()));
        hro.setUserId(UUIDUtil.bytesToString(reservation.getUserId()));
        hro.setHotelId(UUIDUtil.bytesToString(reservation.getHotelId()));
        hro.setHotelName(hotelName);
        hro.setRoomType(roomType);
        hro.setCheckinDate(reservation.getCheckinDate());
        hro.setCheckoutDate(reservation.getCheckoutDate());
        hro.setStatus(reservation.getStatus());
        hro.setReserveTime(reservation.getBookingTime());
        Room room = roomMapper.selectById(reservation.getRoomId());
        if (room != null) {
            long nights = ChronoUnit.DAYS.between(reservation.getCheckinDate(), reservation.getCheckoutDate());
            hro.setTotalPrice(room.getPrice().multiply(BigDecimal.valueOf(nights)));
        }
        return hro;
    }
}

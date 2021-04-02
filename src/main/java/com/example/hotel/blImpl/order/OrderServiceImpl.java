package com.example.hotel.blImpl.order;

import com.example.hotel.enums.OrderState;
import com.example.hotel.bl.hotel.HotelService;
import com.example.hotel.bl.order.OrderService;
import com.example.hotel.bl.user.AccountService;
import com.example.hotel.data.order.OrderMapper;
import com.example.hotel.data.user.AccountMapper;
import com.example.hotel.enums.UserType;
import com.example.hotel.po.Order;
import com.example.hotel.po.User;
import com.example.hotel.vo.OrderVO;
import com.example.hotel.vo.ResponseVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 * @Author: chenyizong
 * @Date: 2020-03-04
 */
@Service
public class OrderServiceImpl implements OrderService {
    private final static String RESERVE_ERROR = "预订失败";
    private final static String ROOMNUM_LACK = "预订房间数量剩余不足";
    private final static String ANNUL_ERROR = "取消失败";
    private final static String STATE_CHANGE_ERROR = "订单修改失败";
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    HotelService hotelService;
    @Autowired
    AccountService accountService;
    @Autowired
    OrderService orderService;

    @Override
    public ResponseVO addOrder(OrderVO orderVO) {
        int reserveRoomNum = orderVO.getRoomNum();
        int curNum = hotelService.getRoomCurNum(orderVO.getHotelId(),orderVO.getRoomType());
        if(reserveRoomNum>curNum){
            return ResponseVO.buildFailure(ROOMNUM_LACK);
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(System.currentTimeMillis());
            String curdate = sf.format(date);
            orderVO.setCreateDate(curdate);
            orderVO.setOrderState("1");
            User user = accountService.getUserInfo(orderVO.getUserId());
            orderVO.setClientName(user.getUserName());
            orderVO.setPhoneNumber(user.getPhoneNumber());
            Order order = new Order();
            BeanUtils.copyProperties(orderVO,order);
            orderMapper.addOrder(order);
            hotelService.updateRoomInfo(orderVO.getHotelId(),orderVO.getRoomType(),orderVO.getRoomNum());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseVO.buildFailure(RESERVE_ERROR);
        }
        return ResponseVO.buildSuccess(true);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderMapper.getAllOrders();
    }

    @Override
    public List<Order> getUserOrders(int userid) {
        return orderMapper.getUserOrders(userid);
    }

    @Override
    public ResponseVO annulOrder(int orderid) {
        //取消订单逻辑的具体实现（注意可能有和别的业务类之间的交互）
        try{
            Order order = orderMapper.getOrderById(orderid);
            int roomNums = order.getRoomNum();
            hotelService.updateRoomInfo(order.getHotelId(),order.getRoomType(),roomNums);
            orderMapper.annulOrder(orderid);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseVO.buildFailure(ANNUL_ERROR);
        }
        return ResponseVO.buildSuccess(true);
    }

    /**
     * @param hotelId
     * @return
     */
    @Override
    public List<Order> getHotelOrders(Integer hotelId) {
        //deal with Circular Dependency
        List<Order> orders = orderService.getAllOrders();
        return orders.stream().filter(order -> order.getHotelId().equals(hotelId)).collect(Collectors.toList());
    }

    @Override
    public ResponseVO updateOrderState(int orderid, String orderState){
        try {
            orderMapper.updateOrderState(orderid,orderState);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseVO.buildFailure(STATE_CHANGE_ERROR);
        }
        return ResponseVO.buildSuccess(true);
    }

    @Override
    public ResponseVO early_checkinOrder(int orderid){
        try{
            Order order = orderMapper.getOrderById(orderid);
            updateOrderState(orderid,"2");
            long living_period = ChronoUnit.DAYS.between(LocalDateTime.parse(order.getCheckOutDate()),LocalDateTime.parse(order.getCheckInDate()));
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime CheckInDate = LocalDateTime.now();
            LocalDateTime CheckOutDate = CheckInDate.plusDays(living_period);
            orderMapper.updateCheckInDate(orderid,CheckInDate.format(dtf));
            orderMapper.updateCheckOutDate(orderid,CheckOutDate.format(dtf));
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseVO.buildFailure(STATE_CHANGE_ERROR);
        }
        return ResponseVO.buildSuccess(true);
    }
    @Override
    public ResponseVO cancelOrder(int orderid, int userid){
        try{
            String usertype = accountMapper.getAccountById(userid).getUserType().toString();
            if(usertype == "1"||usertype == "4"){
                updateOrderState(orderid,"4");
            }
            else{
                updateOrderState(orderid,"5");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseVO.buildFailure(STATE_CHANGE_ERROR);
        }
        return ResponseVO.buildSuccess(true);
    }
}

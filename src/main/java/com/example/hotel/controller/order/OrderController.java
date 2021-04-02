package com.example.hotel.controller.order;

import com.example.hotel.bl.hotel.HotelService;
import com.example.hotel.bl.order.OrderService;
import com.example.hotel.vo.OrderVO;
import com.example.hotel.vo.ResponseVO;
import com.example.hotel.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: chenyizong
 * @Date: 2020-05-26
 */


@RestController()
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/addOrder")
    public ResponseVO reserveHotel(@RequestBody OrderVO orderVO){
        return orderService.addOrder(orderVO);
    }

    @GetMapping("/getAllOrders")
    public ResponseVO retrieveAllOrders(){
        return ResponseVO.buildSuccess(orderService.getAllOrders());
    }

    @GetMapping("/{userid}/getUserOrders")
    public  ResponseVO retrieveUserOrders(@PathVariable int userid){
        return ResponseVO.buildSuccess(orderService.getUserOrders(userid));
    }

    @GetMapping("/{orderid}/annulOrder")
    public ResponseVO annulOrder(@PathVariable int orderid){
        return orderService.annulOrder(orderid);
    }

    @GetMapping("/{hotelId}/allOrders")
    public ResponseVO retrieveHotelOrders(@PathVariable Integer hotelId) {
        return ResponseVO.buildSuccess(orderService.getHotelOrders(hotelId));
    }

    @GetMapping("/{orderId}/early_checkinOrder")
    public ResponseVO early_checkinOrder(@PathVariable Integer orderId) {
        return ResponseVO.buildSuccess(orderService.early_checkinOrder(orderId));
    }

    @PostMapping("/{orderId}/{orderState}/updateOrderState")
    public ResponseVO updateOrderState(@PathVariable Integer orderId,@PathVariable String orderState){
        return ResponseVO.buildSuccess(orderService.updateOrderState(orderId,orderState));
    }

    @GetMapping("{userId}/{orderId}/cancelOrder")
    public ResponseVO cancelOrder(@PathVariable Integer userid,@PathVariable Integer orderId){
        return ResponseVO.buildSuccess(orderService.cancelOrder(orderId,userid));
    }
}


package com.example.hotel.data.order;

import com.example.hotel.po.Order;
import com.example.hotel.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: chenyizong
 * @Date: 2020-03-04
 */
@Mapper
@Repository
public interface OrderMapper {

    int addOrder(Order order);

    List<Order> getAllOrders();

    List<Order> getUserOrders(@Param("userid") int userid);

    int annulOrder(@Param("orderid") int orderid);

    Order getOrderById(@Param("orderid") int orderid);

    void updateOrderState(@Param("orderid") int orderid, @Param("orderState") String orderState);

    void updateCheckInDate(@Param("orderid") int orderid, @Param("CheckInDate") String CheckInDate);

    void updateCheckOutDate(@Param("orderid") int orderid, @Param("CheckOutDate") String CheckOutDate);

    void cancelOrder(@Param("orderid") int orderid, int userid);
}

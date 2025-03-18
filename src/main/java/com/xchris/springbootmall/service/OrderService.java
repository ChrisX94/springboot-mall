package com.xchris.springbootmall.service;

import com.xchris.springbootmall.model.Order;
import dto.CreateOrderRequest;
import dto.OrderQueryParams;

import java.util.List;

public interface OrderService {
    Integer countOrder(OrderQueryParams orderQueryParams);
    List<Order> getOrders(OrderQueryParams orderQueryParams);
    Order getOrderById(Integer orderId);
    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);

}

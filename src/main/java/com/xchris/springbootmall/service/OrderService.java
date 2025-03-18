package com.xchris.springbootmall.service;

import com.xchris.springbootmall.model.Order;
import dto.CreateOrderRequest;

public interface OrderService {

    Order getOrderById(Integer orderId);
    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);

}

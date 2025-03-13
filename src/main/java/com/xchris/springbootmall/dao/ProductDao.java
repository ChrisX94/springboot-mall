package com.xchris.springbootmall.dao;

import com.xchris.springbootmall.model.Product;

public interface ProductDao {
    Product getProductById(Integer productId);
}

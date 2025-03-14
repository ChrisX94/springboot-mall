package com.xchris.springbootmall.dao;

import com.xchris.springbootmall.model.Product;
import dto.ProductRequest;

public interface ProductDao {
    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);
}

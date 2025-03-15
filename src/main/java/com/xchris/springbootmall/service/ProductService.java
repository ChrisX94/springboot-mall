package com.xchris.springbootmall.service;

import com.xchris.springbootmall.model.Product;
import dto.ProductRequest;

public interface ProductService {
    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId, ProductRequest productRequest);

}

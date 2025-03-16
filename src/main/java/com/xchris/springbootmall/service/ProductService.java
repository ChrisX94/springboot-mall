package com.xchris.springbootmall.service;


import com.xchris.springbootmall.dao.ProductQueryParams;
import com.xchris.springbootmall.model.Product;
import dto.ProductRequest;

import java.util.List;

public interface ProductService {

    List<Product> getProducts(ProductQueryParams productQueryParams);
    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId, ProductRequest productRequest);

    void deleteProductById(Integer productId);
}

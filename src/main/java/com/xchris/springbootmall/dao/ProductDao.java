package com.xchris.springbootmall.dao;

import com.xchris.springbootmall.Constant.ProductCategory;
import com.xchris.springbootmall.model.Product;
import dto.ProductRequest;

import java.util.List;

public interface ProductDao {

    List<Product> getProducts(ProductQueryParams productQueryParams);
    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId, ProductRequest productRequest);

    void deleteProductById(Integer productId);
}

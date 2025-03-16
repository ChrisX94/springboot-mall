package com.xchris.springbootmall.controller;

import com.xchris.springbootmall.Constant.ProductCategory;
import com.xchris.springbootmall.dao.ProductQueryParams;
import com.xchris.springbootmall.model.Product;
import com.xchris.springbootmall.service.ProductService;
import dto.ProductRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    // 傳送所有商品資訊
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts(
            // 查詢條件 Filtering
            // 前端可以透過category的參數選擇商品分類, RequestParam(required = false)將category設定成非必要參數
            @RequestParam(required = false) ProductCategory category,
            // 這裡設定一個String search去接搜尋的文字參數
            @RequestParam(required = false) String search,

            // 排序 Sorting
            // 這裡設定根據什麼欄位排序,這裡有設定defaultValue = "created_date" 做為預設值,如果前端沒有傳參數的話就以預設做為參數
            @RequestParam(defaultValue = "created_date")String orderBy,
            // 這裡是決定要用升序或降序來排序,這裡用降序(desc)作為預設值
            @RequestParam(defaultValue = "desc")String sort
            ){

        // 將前端傳來的參數傳入ProductQueryParams
        ProductQueryParams productQueryParams = new ProductQueryParams();
        productQueryParams.setCategory(category);
        productQueryParams.setSearch(search);
        productQueryParams.setOrderBy(orderBy);
        productQueryParams.setSort(sort);


        // 將參數以物件的方式傳入
        List<Product> productList = productService.getProducts(productQueryParams);
        return ResponseEntity.status(HttpStatus.OK).body(productList);
    }


    // CRUD
    // Read
    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer productId) {
        Product product = productService.getProductById(productId);

        if (product != null) {
            return ResponseEntity.status(HttpStatus.OK).body(product);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Create
    @PostMapping("/products") // 新增是用Post方法
    // @RequestBody 要接收 request body 要加上這個註解, 要加上@Valid 在ProductRequest中的@NotNull才會生效
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest) {
        Integer productId = productService.createProduct(productRequest);
        Product product = productService.getProductById(productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    // Update
    @PutMapping("/products/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer productId, // 這裡要去取得要修改的product Id
                                                 @RequestBody @Valid ProductRequest productRequest) { // 這裡是用去取得修改的資訊json
        // 檢查product是否存在
        Product product = productService.getProductById(productId);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        //修改商品的數據
        productService.updateProduct(productId, productRequest);
        Product updateProduct = productService.getProductById(productId);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    // Delete
    @DeleteMapping("products/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId) {
        productService.deleteProductById(productId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

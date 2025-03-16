package com.xchris.springbootmall.controller;

import com.xchris.springbootmall.Constant.ProductCategory;
import com.xchris.springbootmall.dao.ProductQueryParams;
import com.xchris.springbootmall.model.Product;
import com.xchris.springbootmall.service.ProductService;
import com.xchris.springbootmall.util.Page;
import dto.ProductRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated //加上@Validated 才能讓@Max @Min 生效
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    // 傳送所有商品資訊
    @GetMapping("/products")
    public ResponseEntity<Page<Product>> getProducts(
            // 查詢條件 Filtering
            // 前端可以透過category的參數選擇商品分類, RequestParam(required = false)將category設定成非必要參數
            @RequestParam(required = false) ProductCategory category,
            // 這裡設定一個String search去接搜尋的文字參數
            @RequestParam(required = false) String search,

            // 排序 Sorting
            // 這裡設定根據什麼欄位排序,這裡有設定defaultValue = "created_date" 做為預設值,如果前端沒有傳參數的話就以預設做為參數
            @RequestParam(defaultValue = "created_date")String orderBy,
            // 這裡是決定要用升序或降序來排序,這裡用降序(desc)作為預設值
            @RequestParam(defaultValue = "desc")String sort,

            //分頁 Pagination
            // 這裡是用Limit=5的原因是因為在資料量大的狀況下如果不去限制取得的數據會對每次都需要提取大量數據會影響效能
            @RequestParam(defaultValue = "5")@Max(100) @Min(0) Integer limit, // 這次要取得多少數據@Max(100)最多100 @Min(0)最小0
            @RequestParam(defaultValue = "0")@Min(0) Integer offset // 要去跳過多少筆數據

            ){

        // 將前端傳來的參數傳入ProductQueryParams
        ProductQueryParams productQueryParams = new ProductQueryParams();
        productQueryParams.setCategory(category);
        productQueryParams.setSearch(search);
        productQueryParams.setOrderBy(orderBy);
        productQueryParams.setSort(sort);
        productQueryParams.setLimit(limit);
        productQueryParams.setOffset(offset);


        // 將參數以物件的方式傳入去取得product list
        List<Product> productList = productService.getProducts(productQueryParams);

        // 呼叫productService.countProduct(productQueryParams)去取得商品總比數
        Integer total = productService.countProduct(productQueryParams);

        //分頁
        Page<Product> page = new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(total);
        // 這裡是將查詢到的數據回傳到前端
        page.setResult(productList);
        // response body 回傳page
        return ResponseEntity.status(HttpStatus.OK).body(page);
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
        return ResponseEntity.status(HttpStatus.OK).body(updateProduct);
    }

    // Delete
    @DeleteMapping("products/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId) {
        productService.deleteProductById(productId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

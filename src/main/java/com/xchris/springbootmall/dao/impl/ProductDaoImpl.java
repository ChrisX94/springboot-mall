package com.xchris.springbootmall.dao.impl;

import com.xchris.springbootmall.Constant.ProductCategory;
import com.xchris.springbootmall.dao.ProductDao;
import com.xchris.springbootmall.model.Product;
import com.xchris.springbootmall.rowmapper.ProductRowMapper;
import dto.ProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductDaoImpl implements ProductDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<Product> getProducts(ProductCategory category, String search) {
        String sql = "SELECT product_id, product_name, category, image_url," +
                                "price, stock, description, created_date, last_modified_date " +
                                "FROM product WHERE 1=1";
        // WHERE 1=1" 這一段不影響sql，為了能夠用最簡單的方式在後面加上其他查詢的sql,因為有1=1碧唯true所以可以在後面加上 AND 加入其他條件
        Map<String, Object> map = new HashMap<>();
        if(category != null){ // 這裡是去判斷是否有輸入category的參數
            sql = sql + " AND category = :category"; // 要接上的字串開頭要加上空白鍵才不會跟前面的黏再一起
            map.put("category", category.name()); // enum method 要用 .name() 才能轉成字串
        }

        if(search != null){  // 這裡是去判斷是否有輸入search的參數
            sql = sql + " AND product_name LIKE :search";
            map.put("search", "%" + search + "%"); // % 模糊查詢不能加載sql中，一定要加在這裡當作參數輸入才會生效
        }

        List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());
        return productList;
    }

    @Override
    public Product getProductById(Integer productId){
        String sql = "SELECT product_id, product_name, category, image_url, " +
                "price, stock, description, created_date, last_modified_date " +
                "FROM product WHERE product_id =:productId";
        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);

        List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());

        if (productList.size() > 0) {
            return productList.get(0);
        }else{
            return null;
        }
    }

    @Override
    public Integer createProduct(ProductRequest productRequest){
        String sql = "INSERT INTO product(product_name, category, image_url, price, " +
                "stock, description, created_date, last_modified_date)" +
                "VALUES (:productName, :category, :imageUrl, :price, :stock, :description, " +
                ":createDate, :lastModifiedDate)";

        Map<String, Object> map = new HashMap<>();
        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());

        Date now = new Date();
        map.put("createDate", now);
        map.put("lastModifiedDate", now);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        int productId = keyHolder.getKey().intValue();

        return productId;
    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        String sql = "UPDATE product SET product_name = :productName, category = :category, image_url = :imageUrl, " +
                "price = :price, stock = :stock, description = :description, last_modified_date = :lastModifiedDate" +
                " WHERE product_id =:productId";
        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);

        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());

        map.put("lastModifiedDate", new Date());
        namedParameterJdbcTemplate.update(sql, map);
    }

    @Override
    public void deleteProductById(Integer productId) {
        String sql = "DELETE FROM product WHERE product_id =:productId";

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);
        namedParameterJdbcTemplate.update(sql, map);
    }
}

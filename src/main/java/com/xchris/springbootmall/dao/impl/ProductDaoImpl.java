package com.xchris.springbootmall.dao.impl;

import com.xchris.springbootmall.dao.ProductDao;
import dto.ProductQueryParams;
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
    public Integer countProduct(ProductQueryParams productQueryParams) {
        String sql = "SELECT COUNT(*) FROM product WHERE 1=1";

        Map<String, Object> map = new HashMap<>();

        //  查詢物件
        sql = addFilteringSql(sql, map, productQueryParams);

        // namedParameterJdbcTemplate.queryForObject 常用在取count的值, 最後一個參數是表示要回傳一個int的返回值
        Integer total = namedParameterJdbcTemplate.queryForObject(sql, map,Integer.class);

        return total;
    }

    @Override
    // 這裡用ProductQueryParams的物件作接收修改時只要修改dao 跟ProductQueryParams的物件
    public List<Product> getProducts(ProductQueryParams productQueryParams) {
        String sql = "SELECT product_id, product_name, category, image_url," +
                                "price, stock, description, created_date, last_modified_date " +
                                "FROM product WHERE 1=1";
        // WHERE 1=1" 這一段不影響sql，為了能夠用最簡單的方式在後面加上其他查詢的sql,因為有1=1回傳true所以可以在後面加上 AND 加入其他條件

        Map<String, Object> map = new HashMap<>();
        //  查詢物件
        sql = addFilteringSql(sql, map, productQueryParams);

        // 排序
        // 這裡有裡只能用字串拼接方式去做不能用輸入變數的方式, 因為有預設值所以不用在加上條件是檢查
        sql = sql + " ORDER BY " + productQueryParams.getOrderBy() + " " + productQueryParams.getSort();

        // 分頁
        sql = sql + " LIMIT :limit OFFSET :offset";
        map.put("limit", productQueryParams.getLimit());
        map.put("offset", productQueryParams.getOffset());

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

    //將查詢物件寫成方法重複利用
    private String addFilteringSql(String sql, Map<String, Object> map, ProductQueryParams productQueryParams) {
        if(productQueryParams.getCategory() != null){ // 這裡是去判斷是否有輸入category的參數
            sql = sql + " AND category = :category"; // 要接上的字串開頭要加上空白鍵才不會跟前面的黏再一起
            map.put("category", productQueryParams.getCategory().name()); // enum method 要用 .name() 才能轉成字串
        }
        if(productQueryParams.getSearch() != null){  // 這裡是去判斷是否有輸入search的參數
            sql = sql + " AND product_name LIKE :search";
            map.put("search", "%" + productQueryParams.getSearch() + "%"); // % 模糊查詢不能加載sql中，一定要加在這裡當作參數輸入才會生效
        }
        return sql;
    }
}

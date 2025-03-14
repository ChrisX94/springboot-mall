package com.xchris.springbootmall.rowmapper;

import com.xchris.springbootmall.Constant.ProductCategory;
import com.xchris.springbootmall.model.Product;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRowMapper implements RowMapper<Product> {
    @Override
    public Product mapRow(ResultSet resultSet, int i) throws SQLException {
        Product product = new Product();

        product.setProductId(resultSet.getInt("product_id"));
        product.setProductName(resultSet.getString("product_name"));

//        // 因為改用enum這裡用String 變數去接 resultSet.getString("category") 的回傳值(SQL取得的資料)
//        String categoryStr = resultSet.getString("category");
//        // 這裡用在resultSet.getString("category") 的回傳值去enum尋找ProductCategory對應的值
//        ProductCategory category = ProductCategory.valueOf(categoryStr);
//        //將在enum尋找ProductCategory對應的回傳值傳入product.setCategory
//        product.setCategory(category);

        //這裡是直接將SQL取得的資料去轉換成ProductCategory中的enum值，然後傳到product.setCategory中做設定
        product.setCategory(ProductCategory.valueOf(resultSet.getString("category")));

        product.setImageUrl(resultSet.getString("image_url"));
        product.setPrice(resultSet.getInt("price"));
        product.setStock(resultSet.getInt("stock"));
        product.setDescription(resultSet.getString("description"));
        product.setCreateDate(resultSet.getTimestamp("created_date"));
        product.setLaseModifiedDate(resultSet.getTimestamp("last_modified_date"));

        return product;

    }
}

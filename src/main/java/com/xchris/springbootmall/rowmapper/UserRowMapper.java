package com.xchris.springbootmall.rowmapper;



import com.xchris.springbootmall.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet resultset, int i) throws SQLException{
        User user = new User();
        user.setUserId(resultset.getInt("user_id"));
        user.setEmail(resultset.getString("email"));
        user.setPassword(resultset.getString("password"));
        user.setCreatedDate(resultset.getTimestamp("created_date"));
        user.setLastModifiedDate(resultset.getTimestamp("last_modified_date"));

        return user;
    }
}

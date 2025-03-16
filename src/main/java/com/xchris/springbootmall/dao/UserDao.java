package com.xchris.springbootmall.dao;

import com.xchris.springbootmall.model.User;
import dto.UserRegisterRequest;

public interface UserDao {

    User getUserById(Integer userId);
    Integer createUser(UserRegisterRequest userRegisterRequest);
}

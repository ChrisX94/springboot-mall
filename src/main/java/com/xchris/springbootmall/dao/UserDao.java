package com.xchris.springbootmall.dao;

import com.xchris.springbootmall.model.User;
import dto.UserRegisterRequest;

public interface UserDao {

    User getUserById(Integer userId);

    User getUserByEmail(String email);

    Integer createUser(UserRegisterRequest userRegisterRequest);
}

package com.xchris.springbootmall.service;

import com.xchris.springbootmall.model.User;
import dto.UserLoginRequest;
import dto.UserRegisterRequest;

public interface UserService {

    User getUserById(Integer userId);
    Integer register(UserRegisterRequest userRegisterRequest);
    User login(UserLoginRequest userLoginRequest);

}

package com.xchris.springbootmall.service;

import com.xchris.springbootmall.model.User;
import dto.UserRegisterRequest;

public interface UserService {

    User getUserById(Integer userId);
    Integer register(UserRegisterRequest userRegisterRequest);



}

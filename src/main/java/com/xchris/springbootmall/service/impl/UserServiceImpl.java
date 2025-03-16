package com.xchris.springbootmall.service.impl;

import com.xchris.springbootmall.dao.UserDao;
import com.xchris.springbootmall.model.User;
import com.xchris.springbootmall.service.UserService;
import dto.UserRegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        return userDao.createUser(userRegisterRequest);
    }
}

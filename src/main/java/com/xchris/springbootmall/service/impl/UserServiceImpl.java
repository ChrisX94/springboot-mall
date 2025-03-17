package com.xchris.springbootmall.service.impl;

import com.xchris.springbootmall.dao.UserDao;
import com.xchris.springbootmall.model.User;
import com.xchris.springbootmall.service.UserService;
import dto.UserLoginRequest;
import dto.UserRegisterRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UserServiceImpl implements UserService {
    // log
    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        // 檢查註冊的email
        User user = userDao.getUserByEmail(userRegisterRequest.getEmail());
        // 如果email已經被註冊則回傳
        if(user != null){
            log.warn("email {} 已經被註冊" , userRegisterRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 使用 MD5 生成密碼的雜湊值(參數要是byte 型別)
        String hashedPassword = DigestUtils.md5DigestAsHex(userRegisterRequest.getPassword().getBytes());
        userRegisterRequest.setPassword(hashedPassword);

        //  創建帳號
        return userDao.createUser(userRegisterRequest);
    }

    @Override
    public User login(UserLoginRequest userLoginRequest) {
        User user = userDao.getUserByEmail(userLoginRequest.getEmail());

        //  檢查User是否存在
        if(user == null){
            log.warn("Email: {} 尚未註冊" , userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 使用 MD5 生成密碼的雜湊值(參數要是byte 型別)
        String hashedPassword = DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());
        userLoginRequest.setPassword(hashedPassword);

        // 比較密碼
        if (user.getPassword().equals(hashedPassword)){
            return user;
        }else{
            log.warn("Email: {} 密碼不正確" , userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}

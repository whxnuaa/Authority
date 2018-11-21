package com.jit.skiad.serviceimpl;

import com.jit.skiad.mapper.UserMapper;
import com.jit.skiad.serviceinterface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yin on 2017/10/12.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public String getUserImage(String username) {
        return userMapper.getUserImage(username);
    }

    @Override
    public void updateUserImage(String username, String image) {
        userMapper.updateUserImage(username,image);
    }

}

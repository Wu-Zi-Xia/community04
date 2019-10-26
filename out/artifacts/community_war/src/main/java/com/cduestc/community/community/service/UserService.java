package com.cduestc.community.community.service;

import com.cduestc.community.community.mapper.UserMapper;
import com.cduestc.community.community.model.User;
import com.cduestc.community.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;
    public List<User> getUserByAccountId(int id) {
        UserExample example = new UserExample();
        example.createCriteria().andAccountIdEqualTo(id);
        return userMapper.selectByExample(example);
    }

    public void insert(User userModel) {

        userMapper.insert(userModel);
    }

    public void update(User userModel) {
        UserExample example = new UserExample();
        userMapper.updateByExampleSelective(userModel,example);
    }
}

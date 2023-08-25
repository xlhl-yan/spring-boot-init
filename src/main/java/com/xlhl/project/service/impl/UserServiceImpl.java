package com.xlhl.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xlhl.project.common.ErrorCode;
import com.xlhl.project.constant.UserConstant;
import com.xlhl.project.exception.BusinessException;
import com.xlhl.project.mapper.UserMapper;
import com.xlhl.project.model.entity.User;
import com.xlhl.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * UserServiceImpl
 *
 * @author xlhl
 * @version 1.0
 * @description
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }
}

package com.xlhl.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xlhl.project.model.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xlhl
 */
public interface UserService extends IService<User> {

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);


}

package com.xlhl.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xlhl.project.common.BaseResponse;
import com.xlhl.project.common.ErrorCode;
import com.xlhl.project.common.ResultUtils;
import com.xlhl.project.constant.UserConstant;
import com.xlhl.project.exception.BusinessException;
import com.xlhl.project.model.dto.user.UserLoginRequest;
import com.xlhl.project.model.dto.user.UserRegisterRequest;
import com.xlhl.project.model.entity.User;
import com.xlhl.project.model.vo.UserVO;
import com.xlhl.project.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户接口
 *
 * @author xlhl
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("userAccount", userAccount);
        wrapper.eq("userPassword", userPassword);

        // todo 无业务，直接登录
        User user = userService.getOne(wrapper);
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, user);

        return ResultUtils.success(user);
    }


    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(userPassword);

        // todo 无业务，直接添加，使用需要修改
        userService.save(user);
        return ResultUtils.success(user.getId());
    }


    /**
     * 获取当前登录用户
     *
     * @param request
     * @return 当前登录用户信息
     */
    @GetMapping("/current/user")
    public BaseResponse<UserVO> getLoginUser(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return ResultUtils.success(userVO);
    }
}

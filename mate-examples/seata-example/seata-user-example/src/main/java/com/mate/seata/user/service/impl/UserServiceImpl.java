package com.mate.seata.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mate.seata.user.entity.User;
import com.mate.seata.user.mapper.UserMapper;
import com.mate.seata.user.service.IUserService;

/**
 * 用户业务实现类
 *
 * @author pangu
 */
@Transactional
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
}

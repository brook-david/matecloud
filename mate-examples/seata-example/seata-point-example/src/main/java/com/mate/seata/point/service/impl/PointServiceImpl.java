package com.mate.seata.point.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.mate.seata.point.entity.Point;
import com.mate.seata.point.mapper.PointMapper;
import com.mate.seata.point.service.IPointService;

/**
 * 积分业务实现类
 *
 * @author pangu
 */
@Service
public class PointServiceImpl extends ServiceImpl<PointMapper, Point> implements IPointService {
}

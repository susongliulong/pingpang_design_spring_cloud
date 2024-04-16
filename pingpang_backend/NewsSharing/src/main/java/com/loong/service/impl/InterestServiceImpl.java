package com.loong.service.impl;

import com.loong.entity.Interest;
import com.loong.mapper.InterestMapper;
import com.loong.service.IInterestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 描述用户感兴趣的话题 服务实现类
 * </p>
 *
 * @author loong
 * @since 2024-04-15
 */
@Service
public class InterestServiceImpl extends ServiceImpl<InterestMapper, Interest> implements IInterestService {

}

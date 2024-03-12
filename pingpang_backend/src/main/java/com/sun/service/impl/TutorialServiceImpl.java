package com.sun.service.impl;

import com.sun.entity.Tutorial;
import com.sun.mapper.TutorialMapper;
import com.sun.service.ITutorialService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sun
 * @since 2024-03-12
 */
@Service
public class TutorialServiceImpl extends ServiceImpl<TutorialMapper, Tutorial> implements ITutorialService {

}

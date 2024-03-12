package com.sun.service.impl;

import com.sun.entity.Match;
import com.sun.mapper.MatchMapper;
import com.sun.service.IMatchService;
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
public class MatchServiceImpl extends ServiceImpl<MatchMapper, Match> implements IMatchService {

}

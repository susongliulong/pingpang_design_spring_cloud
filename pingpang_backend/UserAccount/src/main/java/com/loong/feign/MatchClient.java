package com.loong.feign;

import com.loong.feign.service.UserService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

@Component("matchClient")
@FeignClient("MatchRegulation")
public interface MatchClient extends UserService {

}

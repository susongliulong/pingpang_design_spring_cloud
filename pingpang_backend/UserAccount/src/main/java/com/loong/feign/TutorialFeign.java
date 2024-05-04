package com.loong.feign;

import com.loong.feign.service.UserService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("TutorialRegulation")
public interface TutorialFeign extends UserService {

}

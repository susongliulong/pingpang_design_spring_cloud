package com.loong.feign;

import com.loong.feign.service.UserService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("CommentRegulation")
public interface CommentFeign extends UserService {

}

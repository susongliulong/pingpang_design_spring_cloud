package com.loong.feign;

import com.loong.feign.service.UserService;
import org.springframework.cloud.openfeign.FeignClient;


@FeignClient(name = "NewsSharing")
public interface NewsSharingClient extends UserService {
}

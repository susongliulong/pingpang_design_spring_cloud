package com.loong.listener;

import com.loong.service.impl.NewsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * 监听 Redis key 过期事件
 */
@Slf4j
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    @Autowired
    private NewsServiceImpl newsService;

    public RedisKeyExpirationListener(RedisMessageListenerContainer redisMessageListenerContainer) {
        super(redisMessageListenerContainer);
    }

    /**
     * 针对 redis 数据失效事件，进行数据处理
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 拿到key
        log.info("监听Redis key过期，key：{}，channel：{}", message.toString(), new String(pattern));

        String messageString = message.toString();
        if(messageString.startsWith("newsId_expire")){

            Long newsId = Long.parseLong(messageString.substring(13));
            // 根据newsId删除指定的文章
            newsService.deleteNewsById(newsId);
        }
    }
}

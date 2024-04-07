package com.loong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class CommentApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(CommentApplication.class,args);
    }
}

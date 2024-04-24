package com.loong;

import com.loong.util.YamlReader;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Objects;


@SpringBootApplication
@EnableTransactionManagement
@EnableFeignClients
@MapperScan("com.loong.mapper")
public class UserAccount
{
    public static void main( String[] args )
    {
        SpringApplication.run(UserAccount.class,args);
    }

    @Bean(name = "avatarPath")
    public String avatarPath(){
        YamlReader.loadResource("/application.yml");
        String path = Objects.requireNonNull(YamlReader.getValueByKey("avatar.location")).toString();
        return path;
    }
}

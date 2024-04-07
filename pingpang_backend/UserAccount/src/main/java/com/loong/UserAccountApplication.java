package com.loong;

import com.loong.util.YamlReader;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Objects;

@SpringBootApplication
@MapperScan("com.loong.mapper")
public class UserAccountApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(UserAccountApplication.class,args);
    }

    @Bean(name = "avatarPath")
    public String avatarPath(){
        YamlReader.loadResource("/application.yml");
        String path = Objects.requireNonNull(YamlReader.getValueByKey("avatar.location")).toString();
        return path;
    }
}

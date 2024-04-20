package com.loong;

import com.loong.util.YamlReader;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan(basePackages = "com.loong.mapper")
public class MatchApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(MatchApplication.class,args);
    }

    @Bean(name = "matchImagePath")
    public String matchImagePath(){
        YamlReader.loadResource("/application.yml");
        return (String) YamlReader.getValueByKey("match.img.path");
    }
}

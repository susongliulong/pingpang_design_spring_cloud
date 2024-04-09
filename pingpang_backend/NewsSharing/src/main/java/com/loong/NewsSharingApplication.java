package com.loong;

import com.loong.util.YamlReader;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.loong.mapper")
public class NewsSharingApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(NewsSharingApplication.class,args);
    }

    @Bean(name="articleImgPath")
    public String articleImgPath(){
        YamlReader.loadResource("/application.yml");
        return (String)YamlReader.getValueByKey("article.img.path");
    }
}

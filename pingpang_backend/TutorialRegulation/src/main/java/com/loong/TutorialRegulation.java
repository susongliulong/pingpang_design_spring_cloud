package com.loong;

import com.loong.util.YamlReader;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.loong.mapper")
@EnableTransactionManagement
public class TutorialRegulation
{
    public static void main( String[] args )
    {
        SpringApplication.run(TutorialRegulation.class,args);
    }

    @Bean(name="tutorialPath")
    public String tutorialPath(){
        YamlReader.loadResource("/application.yml");
        return (String) YamlReader.getValueByKey("tutorial.img.path");
    }
}

package com.loong.config;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.loong.util.YamlReader;

import java.util.Collections;
/**
 * Mybatis Plus代码生成器
 */
public class MPGenerator {

    public static void main(String[] args) {

        YamlReader.loadResource("/application.yml");
        String url = (String) YamlReader.getValueByKey("spring.datasource.url");
        String username = (String) YamlReader.getValueByKey("spring.datasource.username");
        String password = (String) YamlReader.getValueByKey("spring.datasource.password");
        String outputDir = (String) YamlReader.getValueByKey("mp.outputDir");

        FastAutoGenerator.create(url,username,password)
                .globalConfig(builder -> {
                    builder.author("loong") // 设置作者
                            .fileOverride() // 覆盖已生成文件
                            .outputDir(outputDir); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.loong") // 设置父包名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, outputDir)); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("user");
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}


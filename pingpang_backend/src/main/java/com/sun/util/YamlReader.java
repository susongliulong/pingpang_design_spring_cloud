package com.sun.util;

import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class YamlReader {

    // 数据结构设计
    // String->Map<String,Object>
    private static Map<String, Map<String, Object>> properties = new HashMap<>();

    /**
     * 单例
     */

    public static final void loadResource(String fileName) {
        // 清空
        properties.clear();
        Yaml yaml = new Yaml();
        try (InputStream inputStream = YamlReader.class.getResourceAsStream(fileName)) {
            properties = yaml.loadAs(inputStream, HashMap.class);
        } catch (Exception e) {
            log.error("初始化配置文件{}失败", fileName);
        }
    }

    /**
     * get yaml property
     *
     * @param key
     * @return
     */
    public final static Object getValueByKey(String key) {
        // 处理多段key，其中每个key之间以 . 相连
        String separator = ".";
        String[] separatorKeys = null;
        if (key.contains(separator)) {
            separatorKeys = key.split("\\.");
        } else {
            return properties.get(key);
        }
        Map<String, Map<String, Object>> finalValue = new HashMap<>();
        for (int i = 0; i < separatorKeys.length - 1; i++) {
            if (i == 0) {
                finalValue = (Map) properties.get(separatorKeys[i]);
                continue;
            }
            if (finalValue == null) {
                break;
            }
            finalValue = (Map) finalValue.get(separatorKeys[i]);
        }
        return finalValue == null ? null : finalValue.get(separatorKeys[separatorKeys.length - 1]);
    }
}

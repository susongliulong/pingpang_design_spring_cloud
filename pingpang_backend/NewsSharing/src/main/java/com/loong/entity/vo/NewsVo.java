package com.loong.entity.vo;

import com.loong.entity.BasicInformation;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewsVo {

    private Long id;
    private BasicInformation basicInformation;
    private String name; // 分类名称
}

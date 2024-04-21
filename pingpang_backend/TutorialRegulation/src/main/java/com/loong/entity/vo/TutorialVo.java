package com.loong.entity.vo;

import com.loong.entity.BasicInformation;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TutorialVo {
    private BasicInformation basicInformation;
    private String levelName;
    private String suitableObject;
    private long tutorialId;
}

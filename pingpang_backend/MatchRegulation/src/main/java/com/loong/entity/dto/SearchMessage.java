package com.loong.entity.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SearchMessage {

    private Long userId;
    private String keyWord;
    private Integer playerNumber;
    private Integer minPoints;
    private LocalDateTime matchStartTime;
    private LocalDateTime signUpEndTime;
}

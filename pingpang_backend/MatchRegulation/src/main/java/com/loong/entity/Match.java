package com.loong.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author loong
 * @since 2024-04-19
 */
@Data
@NoArgsConstructor
@TableName("matches")
public class Match implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "match_id", type = IdType.AUTO)
    private Long matchId;

    private LocalDateTime signUpEndTime;

    private LocalDateTime matchStartTime;

    private String address;

    private Integer maxNumber;

    private Integer playerNumber;

    private String description;

    private Integer minPoints;

    private Integer awards;

    private String contact;

    private long userId;


}

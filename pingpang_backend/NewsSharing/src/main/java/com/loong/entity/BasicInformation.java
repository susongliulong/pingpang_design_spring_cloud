package com.loong.entity;

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
 * @since 2024-04-09
 */
@Data
@NoArgsConstructor
@TableName("basic_information")
public class BasicInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long authorId;

    private Long categoryId;

    private Long id;

    private String title;

    private String coverImage;

    private LocalDateTime publishTime;

    private String mainContent;

    private Integer likes;

    private Integer comments;

    private Integer collects;

    private Integer pageView;

    private Integer state;

    @Override
    public String toString() {
        return "BasicInformation{" +
                "authorId=" + authorId +
                ", categoryId=" + categoryId +
                ", id=" + id +
                ", title=" + title +
                ", coverImage=" + coverImage +
                ", publishTime=" + publishTime +
                ", mainContent=" + mainContent +
                ", likes=" + likes +
                ", comments=" + comments +
                ", collects=" + collects +
                ", pageView=" + pageView +
                ", state=" + state +
                "}";
    }

}

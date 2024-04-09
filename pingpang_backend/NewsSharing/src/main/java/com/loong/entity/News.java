package com.loong.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
public class News implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "news_id", type = IdType.AUTO)
    private Long newsId;

    private String content;

    private Long categoryId;

}

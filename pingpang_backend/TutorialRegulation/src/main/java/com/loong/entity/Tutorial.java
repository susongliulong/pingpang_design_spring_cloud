package com.loong.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author loong
 * @since 2024-04-21
 */
@Data
@NoArgsConstructor
public class Tutorial implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long tutorialId;

    private Long level;

    private String suitableObject;

    private String content;

    private BasicInformation basicInformation;


}

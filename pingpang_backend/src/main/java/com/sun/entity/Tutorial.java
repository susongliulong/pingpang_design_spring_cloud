package com.sun.entity;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author sun
 * @since 2024-03-12
 */
public class Tutorial implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long tutorialId;

    private String level;

    private String suitableObject;

    private String content;

    public Long getTutorialId() {
        return tutorialId;
    }

    public void setTutorialId(Long tutorialId) {
        this.tutorialId = tutorialId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSuitableObject() {
        return suitableObject;
    }

    public void setSuitableObject(String suitableObject) {
        this.suitableObject = suitableObject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Tutorial{" +
            "tutorialId = " + tutorialId +
            ", level = " + level +
            ", sutiableObject = " + suitableObject +
            ", content = " + content +
        "}";
    }
}

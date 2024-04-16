package com.loong.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 描述用户感兴趣的话题
 * </p>
 *
 * @author loong
 * @since 2024-04-15
 */
public class Interest implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    /**
     * 话题热度
     */
    private Integer hots;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getHots() {
        return hots;
    }

    public void setHots(Integer hots) {
        this.hots = hots;
    }

    @Override
    public String toString() {
        return "Interest{" +
            "id = " + id +
            ", name = " + name +
            ", hots = " + hots +
        "}";
    }
}

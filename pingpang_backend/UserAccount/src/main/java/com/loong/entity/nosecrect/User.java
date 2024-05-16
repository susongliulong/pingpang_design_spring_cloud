package com.loong.entity.nosecrect;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 2L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    private String telephone;

    private String email;

    private String description;

    private String password;

    private String avatar;

    private Integer state;

    private LocalDateTime lastLoginTime;

    private LocalDateTime registerTime;

    private Integer playYears;

}

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
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String telephone;

    private String email;

    private String desc;

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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Account{" +
            "id = " + id +
            ", name = " + name +
            ", telephone = " + telephone +
            ", email = " + email +
            ", desc = " + desc +
        "}";
    }
}

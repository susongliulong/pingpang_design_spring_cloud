package com.loong.entity.menu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Menu {

    private String index;
    private String title;
    private List<MenuItem>menus;
}

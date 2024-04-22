package com.loong.entity.menu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItem {
    private Long index;
    private String title;
    private String href;
}

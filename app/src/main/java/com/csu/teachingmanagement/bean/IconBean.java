package com.csu.teachingmanagement.bean;

import cn.bmob.v3.BmobObject;

/**
 * @author
 * @Date 2020-04-09 10:41
 * 功能：
 */
public class IconBean extends BmobObject {
    private int icon;//图标
    private String name;//图标名字

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IconBean(int icon, String name) {
        this.icon = icon;
        this.name = name;
    }
}

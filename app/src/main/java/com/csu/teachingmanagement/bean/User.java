package com.csu.teachingmanagement.bean;

import cn.bmob.v3.BmobUser;

/**
 * @author
 * @Date 2020-04-08 22:56
 * 功能：
 */
public class User extends BmobUser {
    private String role;
    private String name;


    public String getRole() {
        return role;
    }

    //设置用户类型：0：学生，1：老师
    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

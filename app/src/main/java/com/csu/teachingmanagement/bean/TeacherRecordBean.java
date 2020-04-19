package com.csu.teachingmanagement.bean;

import cn.bmob.v3.BmobObject;

/**
 * @author
 * @Date 2020-04-11 18:14
 * 功能：
 */
public class TeacherRecordBean extends BmobObject {
    private String userId;
    private int createCourse;
    private int deleteCourse;
    private int createFile;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getCreateCourse() {
        return createCourse;
    }

    public void setCreateCourse(int createCourse) {
        this.createCourse = createCourse;
    }

    public int getDeleteCourse() {
        return deleteCourse;
    }

    public void setDeleteCourse(int deleteCourse) {
        this.deleteCourse = deleteCourse;
    }

    public int getCreateFile() {
        return createFile;
    }

    public void setCreateFile(int createFile) {
        this.createFile = createFile;
    }
}

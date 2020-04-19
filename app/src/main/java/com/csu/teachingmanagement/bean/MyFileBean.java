package com.csu.teachingmanagement.bean;

import java.io.File;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * @author
 * @Date 2020-04-12 13:05
 * 功能：
 */
public class MyFileBean extends BmobObject {
    private BmobFile file;

    public BmobFile getFile() {
        return file;
    }

    public void setFile(BmobFile file) {
        this.file = file;
    }
}

package com.csu.teachingmanagement.course;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.csu.base.BaseActivity;
import com.csu.teachingmanagement.R;
import com.csu.teachingmanagement.bean.DiscussBean;
import com.csu.teachingmanagement.bean.ThemeBean;
import com.csu.utils.AccountHelper;
import com.csu.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * @author
 * @Date 2020-04-09 21:26
 * 功能：
 */
public class ReplyActivity extends BaseActivity {

    @BindView(R.id.edt_text)
    EditText mEdtText;
    private String role;
    private DiscussBean bean;

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private String today = df.format(new Date());


    @Override
    public int getLayout() {
        return R.layout.activity_reply;
    }

    @Override
    public void onBindView() {
        role = AccountHelper.getLoginInfo(this,"role");
         bean = (DiscussBean) getIntent().getExtras().getSerializable("discuss");

    }

    @OnClick(R.id.tv_reply)
    public void OnClick(View v){
        ThemeBean data = new ThemeBean();
        data.setIsTeacher(role);
        data.setDiscuss(mEdtText.getText().toString());
        data.setDiscussId(bean.getObjectId());
        data.setName(AccountHelper.getLoginInfo(getApplication(),"name"));
        data.setStudent(AccountHelper.getLoginInfo(getApplication(),"userName"));
        data.setDiscussTime(today);
        data.setCourseId(bean.getCourseId());
        data.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e==null){

                    ToastUtil.showMessage("发表成功");
                    finish();

                }else {
                    ToastUtil.showMessage(e.getMessage());
                }
            }
        });

    }
}

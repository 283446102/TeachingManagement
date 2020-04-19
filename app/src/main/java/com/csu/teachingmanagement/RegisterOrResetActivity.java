package com.csu.teachingmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.csu.base.BaseActivity;
import com.csu.teachingmanagement.bean.StudentCourseBean;
import com.csu.teachingmanagement.bean.TeacherRecordBean;
import com.csu.teachingmanagement.bean.User;
import com.csu.utils.AccountHelper;
import com.csu.utils.L;
import com.csu.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


/**
 * @author
 * @Date 2020-04-08 23:48
 * 功能：注册页面
 */
public class RegisterOrResetActivity extends BaseActivity {
    @BindView(R.id.edt_phone)
    EditText mEditPhone;
    @BindView(R.id.edt_password)
    EditText mEditPassword;
    @BindView(R.id.iv_is_visible)
    ImageView mIvIsVisible;
    @BindView(R.id.rg_type)
    RadioGroup mRgType;
    @BindView(R.id.rb_stu)
    RadioButton mRbStu;
    @BindView(R.id.rb_tea)
    RadioButton mRbTea;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.edt_name)
    EditText mEdtName;
    @BindView(R.id.rl_name)
    RelativeLayout mRlName;
    @BindView(R.id.v_line)
    View mVLine;
    @BindView(R.id.v_line1)
    View mVLine1;
    @BindView(R.id.rl_new_password)
    RelativeLayout mRlNewPassword;
    @BindView(R.id.edt_new_password)
    EditText mEdtNewPassword;
    @BindView(R.id.iv_visible)
    ImageView mIvVisible;

    private boolean isVisible = false;
    private boolean isVisible1 = false;
    private int type;//1是注册，0是重置密码
    private static final int REGISTER = 1;//注册
    private static final int RESET = 0;//重置密码

    private int role = 0;


    @Override
    public int getLayout() {
        return R.layout.activity_register_or_reset;
    }

    @Override
    public void onBindView() {
        //获取登录页面传来的值类型：1：注册，0：重置密码
        Intent intent = getIntent();
        Bundle data = intent.getBundleExtra("data");
        type = data.getInt("type", 0);
        if (type == RESET) {
            mTvTitle.setText("重置密码");
            mRgType.setVisibility(View.GONE);
            mRlName.setVisibility(View.GONE);
            mVLine.setVisibility(View.GONE);
        } else {
            mRlNewPassword.setVisibility(View.GONE);
            mVLine1.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.tv_register, R.id.iv_is_visible, R.id.rb_tea, R.id.rb_stu, R.id.iv_visible})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_register:
                if (type == REGISTER) {//注册
                    register(mEditPhone.getText().toString(), mEditPassword.getText().toString());
                } else {//重置密码
                    resetPassword(mEditPhone.getText().toString(), mEditPassword.getText().toString(), mEdtNewPassword.getText().toString());
                }
                break;
            case R.id.iv_is_visible:
                if (!isVisible) {
                    mIvIsVisible.setImageResource(R.drawable.ic_open_eye);
                    mEditPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isVisible = true;
                } else {
                    mIvIsVisible.setImageResource(R.drawable.ic_close_eye);
                    mEditPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isVisible = false;
                }
                break;
            case R.id.iv_visible:
                if (!isVisible1) {
                    mIvVisible.setImageResource(R.drawable.ic_open_eye);
                    mEdtNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isVisible1 = true;
                } else {
                    mIvVisible.setImageResource(R.drawable.ic_close_eye);
                    mEdtNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isVisible1 = false;
                }
                break;
            case R.id.rb_stu:
                mRbTea.setChecked(false);
                role = 0;
                break;
            case R.id.rb_tea:
                mRbStu.setChecked(false);
                role = 1;
                break;
        }
    }

    /*
     * 重置密码
     * */
    private void resetPassword(String userId, String oldPassword, String newPassword) {
        if (oldPassword.equals(newPassword)) {
            ToastUtil.showMessage("新密码不能和旧密码一样，请重新输入");
            return;
        }
        if (userId.equals("")) {
            ToastUtil.showMessage("请输入账号");
            return;
        }
        if (oldPassword.equals("")) {
            ToastUtil.showMessage("请输入旧密码");
            return;
        }
        if (newPassword.equals("")) {
            ToastUtil.showMessage("请输入新密码");
            return;
        }

        User user = new User();
        user.setUsername(userId);
        user.setPassword(oldPassword);
        user.login(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    user.setPassword(newPassword);
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                ToastUtil.showMessage("密码修改成功");
                                finish();
                            } else {
                                ToastUtil.showMessage("修改密码失败,错误码:" + e.getMessage());
                            }
                        }
                    });
                } else {
                    ToastUtil.showMessage("旧密码错误，请重新输入");
                }
            }
        });
    }


    /*
     * 注册
     * */
    private void register(String userName, String password) {
        if (mEdtName.getText().toString().equals("")) {
            ToastUtil.showMessage("请输入你的真实名字");
            return;
        }
        if (password.equals("")) {
            ToastUtil.showMessage("请输入密码");
            return;
        }

        saveInfo(userName, password);
    }


    /**
     * @param userName
     * @param password 注册用户保存到user表中
     */
    private void saveInfo(final String userName, final String password) {
        User user = new User();
        user.setUsername(userName);
        user.setPassword(password);
        user.setName(mEdtName.getText().toString());
        user.setRole(role + "");
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    if (role == 1) {
                        TeacherRecordBean bean = new TeacherRecordBean();
                        bean.setUserId(user.getUsername());
                        bean.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e != null) {
                                    ToastUtil.showMessage("创建老师课程表数据失败");
                                }
                            }
                        });
                    } else {
                        StudentCourseBean bean1 = new StudentCourseBean();
                        bean1.setUserId(user.getUsername());
                        bean1.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e != null) {
                                    ToastUtil.showMessage("创建学生课程表数据失败");
                                }
                            }
                        });
                    }
                    ToastUtil.showMessage("注册成功");
                    finish();
                } else {
                    ToastUtil.showMessage(e.getMessage());
                }
            }
        });
    }
}

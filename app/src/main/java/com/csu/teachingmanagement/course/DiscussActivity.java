package com.csu.teachingmanagement.course;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.csu.base.BaseActivity;
import com.csu.teachingmanagement.R;
import com.csu.teachingmanagement.bean.CourseBean;
import com.csu.teachingmanagement.bean.DiscussBean;
import com.csu.teachingmanagement.bean.TeacherRecordBean;
import com.csu.utils.AccountHelper;
import com.csu.utils.ToastUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * @author
 * @Date 2020-04-11 13:42
 * 功能：
 */
public class DiscussActivity extends BaseActivity {

    @BindView(R.id.edt_discuss)
    EditText mEdtDiscuss;
    @BindView(R.id.edt_text)
    EditText mEdtText;
    @BindView(R.id.tv_add)
    TextView mTvAdd;
    @BindView(R.id.tv_file_name)
    TextView mTvFileName;
    @BindView(R.id.tv_select_file)
    TextView mTvSelectFile;

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private String today = df.format(new Date());
    private String path = "";

    @Override
    public int getLayout() {
        return R.layout.activity_discuss;
    }

    @Override
    public void onBindView() {
        CourseBean data = (CourseBean) getIntent().getExtras().getSerializable("course_info");

        if (data != null) {
            mTvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mEdtDiscuss.getText().toString().equals("")) {
                        ToastUtil.showMessage("请输入讨论标题");
                        return;
                    }
                    if (mEdtText.getText().toString().equals("")) {
                        ToastUtil.showMessage("请输入讨论内容");
                        return;
                    }


                    if (!path.equals("")){
                        BmobFile file = new BmobFile(new File(path));
                        file.upload(new UploadFileListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e==null){
                                    DiscussBean bean = new DiscussBean();
                                    bean.setFile(file);
                                    bean.setTitle(mEdtDiscuss.getText().toString());
                                    bean.setDiscuss(mEdtText.getText().toString());
                                    bean.setCourseId(data.getObjectId());
                                    bean.setCourseName(data.getName());
                                    bean.setDay(today);
                                    bean.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String s, BmobException e) {
                                            if (e == null) {
                                                BmobQuery<TeacherRecordBean> query = new BmobQuery<>();
                                                query.addWhereEqualTo("userId", AccountHelper.getLoginInfo(getApplication(),"userName"));
                                                query.findObjects(new FindListener<TeacherRecordBean>() {
                                                    @Override
                                                    public void done(List<TeacherRecordBean> object, BmobException e) {
                                                        if (e==null){
                                                            TeacherRecordBean bean1 = object.get(0);
                                                            bean1.setCreateFile(bean1.getCreateFile()+1);
                                                            bean.update(new UpdateListener() {
                                                                @Override
                                                                public void done(BmobException e) {
                                                                    if (e==null){
                                                                        ToastUtil.showMessage("保存成功");
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }
                                                });
                                                finish();
                                            } else {
                                                ToastUtil.showMessage("创建失败");
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }else {
                        DiscussBean bean = new DiscussBean();
                        bean.setTitle(mEdtDiscuss.getText().toString());
                        bean.setDiscuss(mEdtText.getText().toString());
                        bean.setCourseId(data.getObjectId());
                        bean.setCourseName(data.getName());
                        bean.setDay(today);
                        bean.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    ToastUtil.showMessage("保存成功");
                                    finish();
                                } else {
                                    ToastUtil.showMessage("创建失败");
                                }
                            }
                        });
                    }

                }
            });

            mTvSelectFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");//无类型限制
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(intent, 1);
                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if ("file".equalsIgnoreCase(uri.getScheme())) {//使用第三方应用打开
                path = uri.getPath();
                return;
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                path = getPath(this, uri);
            } else {//4.4以下下系统调用方法
                path = getRealPathFromURI(uri);
            }

            String []name = path.split("/");
            mTvFileName.setText("文件名："+name[name.length-1]);
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (null != cursor && cursor.moveToFirst()) {
            ;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }
        return res;
    }

    /**
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     */
    @SuppressLint("NewApi")
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}

package com.ypl.selectlocalpicture.selectPic.model;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.ypl.selectlocalpicture.R;
import com.ypl.selectlocalpicture.selectPic.AppConst;
import com.ypl.selectlocalpicture.selectPic.GetPathAndCrop;
import com.ypl.selectlocalpicture.selectPic.MultPicsMudule;
import com.ypl.selectlocalpicture.selectPic.SelectMultiplyPicsActivity;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by 00829bill on 2018/6/20.
 * 弹窗功能的实现类
 */
public class SelectTheSysPicModel implements ISelectTheSysPicModel {

    private Activity activity;

    public SelectTheSysPicModel(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void openCamera() {  //打开相机
        Uri uriForFile;
        File file = GetPathAndCrop.getInstance().getSelectPicFile(activity);
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            uriForFile = Uri.fromFile(file);
        } else {
            uriForFile = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileProvider", file);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile);
        activity.startActivityForResult(intent, AppConst.OPENCAMERA);
    }

    @Override
    public void openGallery() {  //打开相册
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        activity.startActivityForResult(intent, AppConst.OPENGALLERY);
    }

    @Override
    public void selectMultiplyPics() {  //选择多张图片
        ArrayList<MultPicsMudule> pics = new ArrayList<>();
        Cursor cursor = activity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));        //获取图片的名称
                byte[] data = cursor.getBlob(cursor.getColumnIndex(MediaStore.Images.Media.DATA));      //获取图片的地址
                pics.add(new MultPicsMudule(activity.getString(R.string.mult_pic_name, new String(data, 0, data.length - 1)), name, false));
            }
            cursor.close();
            Intent intent = new Intent(activity, SelectMultiplyPicsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(AppConst.MULTIPLYPICS, pics);
            activity.startActivityForResult(intent, AppConst.OPENMULTIPLYPICS);
        }
    }
}

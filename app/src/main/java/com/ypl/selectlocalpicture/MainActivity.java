package com.ypl.selectlocalpicture;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ypl.selectlocalpicture.permission.PermissionRequestImpl;
import com.ypl.selectlocalpicture.permission.PermissionResultListener;
import com.ypl.selectlocalpicture.selectPic.AppConst;
import com.ypl.selectlocalpicture.selectPic.GetPathAndCrop;
import com.ypl.selectlocalpicture.selectPic.SelectTheSysPicDialog;
import com.ypl.selectlocalpicture.selectPic.presenter.SelectTheSysPicPresenter;

import java.io.File;
import java.io.IOException;

/**
 * author: 00829bill
 * date: 2018/9/5 15:43
 * describe:选择本地图片使用
 */
public class MainActivity extends AppCompatActivity implements SelectTheSysPicDialog.OnDialogItemClickListener, PermissionResultListener {

    private SelectTheSysPicPresenter presenter;
    private PermissionRequestImpl requestImpl;
    private ImageView showPicView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new SelectTheSysPicPresenter(MainActivity.this);
        requestImpl = new PermissionRequestImpl(MainActivity.this, this);
        showPicView = findViewById(R.id.showSelectPic);
        findViewById(R.id.selectPic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SelectTheSysPicDialog(MainActivity.this, R.style.DialogPopTheme, true, MainActivity.this).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestImpl != null)
            requestImpl.onPermissionCallBack(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case AppConst.OPENCAMERA:      //裁剪拍照图片
                    GetPathAndCrop.getInstance().cropCameraPic(this, 0, 0);
                    break;
                case AppConst.OPENGALLERY:   //裁剪相册图片
                    try {
                        GetPathAndCrop.getInstance().cropGalleryPic(this, data.getData(), 0, 0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case AppConst.CROP_IMAGE:        //裁剪图片File的回调
                    File selectPicCropFile = GetPathAndCrop.getInstance().getSelectPicCropFile(MainActivity.this);
                    Glide.with(getApplicationContext()).load(selectPicCropFile).into(showPicView);
                    break;
            }
    }

    @Override
    public void openCamera() {
        if (requestImpl != null)
            requestImpl.requestPermission(new String[]{Manifest.permission.CAMERA});
    }

    @Override
    public void openGallery() {
        if (requestImpl != null)
            requestImpl.requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});
    }

    @Override
    public void openMultyPic() {

    }

    @Override
    public void granted(String permission) {
        switch (permission) {
            case Manifest.permission.CAMERA:
                if (presenter != null)
                    presenter.openCamera();
                break;
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                if (presenter != null)
                    presenter.openGallery();
                break;
        }
    }

    @Override
    public void denied(String permission) {

    }

    @Override
    public void deniedForever(String permission) {

    }
}

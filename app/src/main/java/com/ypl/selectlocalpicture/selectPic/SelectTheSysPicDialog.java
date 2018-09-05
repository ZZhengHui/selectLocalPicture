package com.ypl.selectlocalpicture.selectPic;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ypl.selectlocalpicture.R;


/**
 * Created by 00829bill on 2018/6/20.
 * 选择系统图片弹框
 */
public class SelectTheSysPicDialog extends Dialog implements View.OnClickListener {
    private Activity activity;
    private Boolean isCancelable;
    private OnDialogItemClickListener itemClickListener;

    public SelectTheSysPicDialog(@NonNull Activity activity, int themeResId, boolean isCancelable, OnDialogItemClickListener itemClickListener) {
        super(activity, themeResId);
        this.activity = activity;
        this.isCancelable = isCancelable;
        this.itemClickListener = itemClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_sys_pic);
        setCancelable(isCancelable);
        initView();
    }

    private void initView() {
        Window window = getWindow();
        if (window != null) {
            window.setGravity(Gravity.BOTTOM);
            WindowManager manager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics metrics = new DisplayMetrics();
            if (manager != null) {
                manager.getDefaultDisplay().getMetrics(metrics);
            }
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.height = (int) (metrics.heightPixels * 0.3);
            window.setAttributes(attributes);
        }
        findViewById(R.id.open_camera).setOnClickListener(this);
        findViewById(R.id.open_gallery).setOnClickListener(this);
        findViewById(R.id.choose_multiply_pic).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.open_camera:
                itemClickListener.openCamera();
                break;
            case R.id.open_gallery:
                itemClickListener.openGallery();
                break;
            case R.id.choose_multiply_pic:
                itemClickListener.openMultyPic();
                break;
        }
        dismiss();
    }

    public interface OnDialogItemClickListener {
        void openCamera();

        void openGallery();

        void openMultyPic();
    }
}

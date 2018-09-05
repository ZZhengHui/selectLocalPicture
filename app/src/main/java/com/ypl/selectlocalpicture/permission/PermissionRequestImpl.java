package com.ypl.selectlocalpicture.permission;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.ypl.selectlocalpicture.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * author: 00829bill
 * date: 2018/3/28 14:36
 * describe:请求权限
 */
public class PermissionRequestImpl implements PermissionRequestListener {
    private Activity activity;
    private HashMap<String, String> pers;
    private PermissionResultListener callBack;

    public PermissionRequestImpl(Activity activity, PermissionResultListener callBack) {
        this.activity = activity;
        this.callBack = callBack;
    }

    @Override
    public void requestPermission(String[] permissions) {
        pers = new HashMap<>();
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                pers.put(permission, permission);   //加入未申请的权限
            } else {
                callBack.granted(permission);
            }
        }
        if (pers.size() > 0) {
            ArrayList<String> permission = new ArrayList<>();
            for (Map.Entry<String, String> per : pers.entrySet()) {
                permission.add(per.getValue());
            }
            String[] perms = permission.toArray(new String[pers.size()]);
            ActivityCompat.requestPermissions(activity, perms, 0x001);
        }
    }

    @Override
    public void onPermissionCallBack(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 1)
            for (int i = 0; i < permissions.length; i++) {
                grantedPermission(permissions[i], grantResults, i);
            }
        else grantedPermission(permissions[0], grantResults, 0);
    }

    private void grantedPermission(String permission, int[] grantResults, int position) {
        if (grantResults[position] == PackageManager.PERMISSION_GRANTED) {
            pers.remove(permission);
            callBack.granted(permission);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                callBack.denied(permission);
            } else {
                lackOfPermission(permission);
            }
        }
    }

    private void lackOfPermission(String permission) {
        String perName = "";
        switch (permission) {
            case Manifest.permission.CALL_PHONE:
                perName = "打电话";
                break;
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                perName = "读写本地文件";
                break;
            case Manifest.permission.SEND_SMS:
                perName = "发短信";
                break;
            case Manifest.permission.CAMERA:
                perName = "拍照";
                break;
        }
        Toast.makeText(activity, activity.getString(R.string.lack_of_permission, perName), Toast.LENGTH_SHORT).show();
        callBack.deniedForever(perName);
    }
}

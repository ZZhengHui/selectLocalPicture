package com.ypl.selectlocalpicture.permission;

import android.support.annotation.NonNull;

/**
 * author: 00829bill
 * date: 2018/3/28 14:50
 * describe:申请权限功能
 */
public interface PermissionRequestListener {

    void requestPermission(String[] permissions);

    void onPermissionCallBack(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

}

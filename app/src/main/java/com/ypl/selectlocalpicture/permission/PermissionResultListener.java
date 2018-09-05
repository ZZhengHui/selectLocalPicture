package com.ypl.selectlocalpicture.permission;

/**
 * author: 00829bill
 * date: 2018/3/29 16:08
 * describe:申请权限的回调
 */
public interface PermissionResultListener {

    void granted(String permission);

    void denied(String permission);

    void deniedForever(String permission);
}

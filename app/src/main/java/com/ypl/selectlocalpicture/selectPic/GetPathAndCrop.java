package com.ypl.selectlocalpicture.selectPic;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by 00829bill on 2018/6/21.
 * 选择图片保存的路径和裁剪方法
 */
public class GetPathAndCrop {
    private static GetPathAndCrop util;

    public static GetPathAndCrop getInstance() {
        if (util == null) {
            util = new GetPathAndCrop();
        }
        return util;
    }

    public File getSelectPicFile(Activity activity) {   //选择图片的路径
        return new File(activity.getExternalCacheDir(), "pic.jpg");
    }

    public File getSelectPicCropFile(Activity activity) {   //裁剪的图片保存的路径
        return new File(activity.getExternalCacheDir(), "crop.jpg");
    }

    /**
     * 请求裁剪拍照的图片
     */
    public void cropCameraPic(Activity activity, int aspectX, int aspectY) {
        File picFile = getSelectPicFile(activity);
        if (picFile.exists()) {
            File selectPicCropFile = getSelectPicCropFile(activity);
            if (!TextUtils.isEmpty(picFile.getAbsolutePath())) {
                Uri uri;
                Uri outputUri;
                outputUri = Uri.fromFile(selectPicCropFile);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    uri = Uri.fromFile(picFile);
                } else {
                    uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileProvider", picFile);
                }
                activity.startActivityForResult(crop(uri, outputUri, aspectX, aspectY), AppConst.CROP_IMAGE);
            }
        }
    }

    /**
     * 请求裁剪选择图片
     */
    public void cropGalleryPic(Activity activity, Uri imageUri, int aspectX, int aspectY) throws IOException {
        String imagePath = "";
        String content = "com.android.providers.media.documents";
        String content1 = "com.android.downloads.documents";
        if (DocumentsContract.isDocumentUri(activity, imageUri)) {
            String documentId = DocumentsContract.getDocumentId(imageUri);
            if (TextUtils.equals(content, imageUri.getAuthority())) {
                String id = documentId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                Uri externalContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                imagePath = getImagePath(activity, selection, externalContentUri);
            } else if (TextUtils.equals(content1, imageUri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                imagePath = getImagePath(activity, null, contentUri);
            }
        } else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            imagePath = getImagePath(activity, null, imageUri);
        } else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            imagePath = imageUri.getPath();
        }
        File selectPicCropFile = getSelectPicCropFile(activity);
        if (selectPicCropFile.exists()) {
            selectPicCropFile.delete();
        }
        selectPicCropFile.createNewFile();
        if (!TextUtils.isEmpty(imagePath)) {
            activity.startActivityForResult(crop(imageUri, Uri.fromFile(selectPicCropFile), aspectX, aspectY), AppConst.CROP_IMAGE);
        }
    }

    /**
     * 上传本地选择的图片
     */
    /*public void uploadLocalSelectPic(Activity activity, UploadLocalPicListener listener) {
        File selectPicCropFile = getSelectPicCropFile(activity);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), selectPicCropFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", selectPicCropFile.getAbsolutePath(), requestBody);
        RequestApi.Companion.getInstance().uploadFiles(body)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {

                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(uploadResponseBean -> {
                    if (uploadResponseBean.isSuccess()) {
                        listener.getLocalPic(uploadResponseBean.getUrl());
                    } else {
                        ToastUtils.Companion.show(uploadResponseBean.getMsg());
                    }
                }, throwable -> Log.e("upload Error>", throwable.toString()));
    }*/

    private String getImagePath(Activity activity, String selection, Uri externalContentUri) {
        String imagePath = "";
        Cursor cursor = activity.getContentResolver().query(externalContentUri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return imagePath;
    }

    /**
     * 裁剪方法
     */
    private Intent crop(Uri uri, Uri outputUri, int aspectX, int aspectY) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");    // 设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("aspectX", aspectX);      //该参数可以不设定 用来规定裁剪区的宽高比
        intent.putExtra("aspectY", aspectY);
//        intent.putExtra("outputX", 100);      //该参数设定imageView的大小
//        intent.putExtra("outputY", 49);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());     //输出图片的格式
        intent.putExtra("return-data", false);      //是否返回bitmap对象
        intent.putExtra("noFaceDetection", true);
        return intent;
    }


}

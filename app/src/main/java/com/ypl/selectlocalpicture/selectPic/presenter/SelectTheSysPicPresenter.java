package com.ypl.selectlocalpicture.selectPic.presenter;

import android.app.Activity;

import com.ypl.selectlocalpicture.selectPic.model.ISelectTheSysPicModel;
import com.ypl.selectlocalpicture.selectPic.model.SelectTheSysPicModel;


/**
 * Created by 00829bill on 2018/6/20.
 * 获取选择的图片
 */
public class SelectTheSysPicPresenter implements ISelectTheSysPicPresenter {

    private ISelectTheSysPicModel model;

    public SelectTheSysPicPresenter(Activity activity) {
        model = new SelectTheSysPicModel(activity);
    }

    @Override
    public void openCamera() {
        model.openCamera();
    }

    @Override
    public void openGallery() {
        model.openGallery();
    }

    @Override
    public void openMultyPic() {
        model.selectMultiplyPics();
    }
}

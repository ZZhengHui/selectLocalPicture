package com.ypl.selectlocalpicture.selectPic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ypl.selectlocalpicture.R;

import java.util.ArrayList;

/**
 * Created by bill on 2018/7/22.
 * 多张图片预览大图
 */
public class SelectMultiplyPicPreviewActivity extends AppCompatActivity implements View.OnClickListener {

    public static String PICS = "pics";
    private ViewPager previewPager;
    private PreviewPagerAdapter previewPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_preview_layout);
        initView();
        initData();
        initClick();
    }

    private void initView() {
        previewPager = findViewById(R.id.selectPicPrevewPager);
    }

    private void initData() {
        ArrayList<MultPicsMudule> pics = (ArrayList<MultPicsMudule>) getIntent().getSerializableExtra(PICS);
        if (previewPagerAdapter == null) {
            previewPagerAdapter = new PreviewPagerAdapter(pics);
            previewPager.setAdapter(previewPagerAdapter);
        } else previewPagerAdapter.notifyDataSetChanged();
    }

    private void initClick() {
        findViewById(R.id.selectPicDoneTv).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.selectPicDoneTv:      //回调当前选择的图片

                break;
        }
    }

    class PreviewPagerAdapter extends PagerAdapter {
        private ArrayList<MultPicsMudule> datas = new ArrayList<>();

        PreviewPagerAdapter(ArrayList<MultPicsMudule> pics) {
            this.datas = pics;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(datas.get(position).getPath()).into(imageView);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}

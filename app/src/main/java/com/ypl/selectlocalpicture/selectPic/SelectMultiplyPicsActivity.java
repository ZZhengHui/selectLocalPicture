package com.ypl.selectlocalpicture.selectPic;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ypl.selectlocalpicture.R;

import java.util.ArrayList;

/**
 * author: 00829bill
 * date: 2018/7/3 9:13
 * describe:选择多张图片
 */
public class SelectMultiplyPicsActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String PARAM_SELECT_MULTIPLY_PICS = "param_select_multiply_pics";
    private MultiplyPicsAdapter adapter;
    private ArrayList<MultPicsMudule> selectPics = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_multiply_layout);
        ArrayList<MultPicsMudule> pics = (ArrayList<MultPicsMudule>) getIntent().getSerializableExtra(AppConst.MULTIPLYPICS);
        RecyclerView pRecycler = findViewById(R.id.multyPicsRecycler);
        findViewById(R.id.multiplyBackArea).setOnClickListener(this);
        findViewById(R.id.doneTv).setOnClickListener(this);

        pRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        final int spacing = getResources().getDimensionPixelOffset(R.dimen.dp10);
        pRecycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.left = spacing / 2;
                outRect.bottom = spacing;
                outRect.right = spacing / 2;
            }
        });
        if (adapter == null) {
            adapter = new MultiplyPicsAdapter(pics);
            pRecycler.setAdapter(adapter);
        } else adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.multiplyBackArea:
                onBackPressed();
                break;
            case R.id.doneTv:   //回调选择的图片
                if (selectPics.size() > 0) {
                    selectPics.clear();
                }
                if (adapter != null) {
                    for (MultPicsMudule pic : adapter.pics) {
                        if (pic.isCheck()) {
                            selectPics.add(pic);
                        }
                    }
                }
                Intent intent = getIntent();
                intent.putExtra(PARAM_SELECT_MULTIPLY_PICS, selectPics);
                setResult(RESULT_OK, intent);
                SelectMultiplyPicsActivity.this.finish();
                break;
        }
    }

    class MultiplyPicsAdapter extends RecyclerView.Adapter<MultiplyPicsAdapter.MultiplyPicsHolder> {

        private ArrayList<MultPicsMudule> pics = new ArrayList<>();

        MultiplyPicsAdapter(ArrayList<MultPicsMudule> pics) {
            this.pics = pics;
        }

        @Override
        public MultiplyPicsAdapter.MultiplyPicsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_multiply_pic, parent, false);
            return new MultiplyPicsHolder(view);
        }

        @Override
        public void onBindViewHolder(MultiplyPicsAdapter.MultiplyPicsHolder holder, int position) {
            holder.bind(pics);
        }

        @Override
        public int getItemCount() {
            return pics.size();
        }

        class MultiplyPicsHolder extends RecyclerView.ViewHolder {

            private final ImageView selectPic;
            private final CheckBox checkPic;

            MultiplyPicsHolder(View itemView) {
                super(itemView);
                selectPic = itemView.findViewById(R.id.selectPic);
                checkPic = itemView.findViewById(R.id.checkPic);
            }

            void bind(final ArrayList<MultPicsMudule> pics) {
                Glide.with(SelectMultiplyPicsActivity.this).load(pics.get(getLayoutPosition()).getPath()).into(selectPic);
                checkPic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        pics.get(getLayoutPosition()).setCheck(isChecked);
                    }
                });
                checkPic.setChecked(pics.get(getLayoutPosition()).isCheck());
            }
        }
    }
}

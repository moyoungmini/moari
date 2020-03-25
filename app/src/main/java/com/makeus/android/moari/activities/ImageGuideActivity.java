package com.makeus.android.moari.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.gson.JsonObject;
import com.makeus.android.moari.MoariApp;
import com.makeus.android.moari.R;
import com.makeus.android.moari.adapters.ImageGuideAdapter;
import com.makeus.android.moari.datas.ImageGuideData;
import com.makeus.android.moari.responses.LoginResponse;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

import static com.makeus.android.moari.MoariApp.MEDIA_TYPE_JSON;
import static com.makeus.android.moari.MoariApp.X_ACCESS_TOKEN;
import static com.makeus.android.moari.MoariApp.catchAllThrowable;

public class ImageGuideActivity extends SuperActivity {
    private ViewPager2 viewpager2;
    private ImageGuideAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_guide);

        initViews();

        adapter = new ImageGuideAdapter(this);

        ImageGuideData data1 = new ImageGuideData();
        data1.setImage(R.drawable.guide_1);
        ImageGuideData data2 = new ImageGuideData();
        data2.setImage(R.drawable.guide_2);
        ImageGuideData data3 = new ImageGuideData();
        data3.setImage(R.drawable.guide_3);
        ImageGuideData data4 = new ImageGuideData();
        data4.setImage(R.drawable.guide_4);
        ImageGuideData data5 = new ImageGuideData();
        data5.setImage(R.drawable.guide_5);
        ImageGuideData data6 = new ImageGuideData();
        data6.setImage(R.drawable.guide_6);

        adapter.addData(data1);
        adapter.addData(data2);
        adapter.addData(data3);
        adapter.addData(data4);
        adapter.addData(data5);
        adapter.addData(data6);


        viewpager2.setAdapter(adapter);
        viewpager2.setOrientation(viewpager2.ORIENTATION_HORIZONTAL);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.amin_slide_in_right, R.anim.amin_slide_out_left);
    }

    @Override
    void initViews() {
        viewpager2 = findViewById(R.id.image_guide_viewpager2);
    }
}
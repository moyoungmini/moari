package com.makeus.android.moari.adapters;

import android.app.Activity;
import android.content.Context;
import android.transition.TransitionManager;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.makeus.android.moari.R;
import com.makeus.android.moari.datas.CategoryData;

import java.util.ArrayList;
import java.util.Collections;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CategoryAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<CategoryData> listData;

    public CategoryAdapter(Context _context, ArrayList<CategoryData> _listData) {
        this.context = _context;
        this.listData = _listData;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.category_item, null);

        TextView title, content, oneLine;
        title = view.findViewById(R.id.category_item_title_tv);
        content = view.findViewById(R.id.category_item_content_tv);
        oneLine = view.findViewById(R.id.category_item_oneline_tv);

        title.setText(listData.get(position).getTitle());

        container.addView(view);

        return view;
    }

}
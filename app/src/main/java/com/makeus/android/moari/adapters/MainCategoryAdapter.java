package com.makeus.android.moari.adapters;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeus.android.moari.R;
import com.makeus.android.moari.activities.ReviewEditActivity;
import com.makeus.android.moari.datas.CategoryData;
import com.makeus.android.moari.datas.CurationData;

import java.util.ArrayList;

public class MainCategoryAdapter extends RecyclerView.Adapter<MainCategoryAdapter.ItemViewHolder> {

    private Activity activity;
    private ArrayList<CategoryData> listData;

    public MainCategoryAdapter(Activity activity, ArrayList<CategoryData> listData) {
        this.activity = activity;
        this.listData = listData;
    }
    // constructor


    public ArrayList<CategoryData> getListData() {
        return listData;
    }

    public void clearData() {
        this.listData.clear();
    }

    public void  addData(CategoryData data) {
        listData.add(data);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        int height = parent.getMeasuredWidth()/3;
        view.setMinimumHeight(height);
        return new ItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, final int position) {
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView category;
        private LinearLayout layout;

        ItemViewHolder(View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.main_category_item_tv);
            layout = itemView.findViewById(R.id.main_category_item_layout);
            int position = getAdapterPosition();
            if(position<0) {
                return;
            }
            Log.i("SDF", String.valueOf(position));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listData.get(position).isSelct()) {

                    }
                    // 존재 o - 해당 카테고리로 들어간다.
                    else {

                    }
                    // 존재 x - 카테고리 추가

                }
            });
            // item click method

            if(listData.get(position).isSelct()) {
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        return false;
                    }
                });
                // 선택 다이어로그 시작
            }
        }

        void onBind(CategoryData data) {
            int position = getAdapterPosition();


            category.setText(data.getCategoryName());
            if(data.isSelct()) {
                category.setTextColor(activity.getResources().getColorStateList(R.color.colorWhite));
                layout.setBackgroundResource(R.drawable.main_exist_img);
            }
            else {
                category.setTextColor(activity.getResources().getColorStateList(R.color.colorWhiteOpacity));
                layout.setBackgroundResource(R.drawable.main_empty_img);
            }

        }
        // set views
    }
}
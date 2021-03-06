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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeus.android.moari.R;
import com.makeus.android.moari.activities.ReviewEditActivity;
import com.makeus.android.moari.activities.ReviewListActivity;
import com.makeus.android.moari.datas.CategoryData;
import com.makeus.android.moari.datas.CurationData;
import com.makeus.android.moari.dialogs.CategoryChangeDialog;
import com.makeus.android.moari.dialogs.CategorySelectDialog;
import com.makeus.android.moari.interfaces.DialogCategorySelectInterface;

import java.util.ArrayList;

public class MainCategoryAdapter extends RecyclerView.Adapter<MainCategoryAdapter.ItemViewHolder> {

    private Activity activity;
    private ArrayList<CategoryData> listData;
    private DialogCategorySelectInterface selectInterface;

    public MainCategoryAdapter(Activity activity, ArrayList<CategoryData> listData, DialogCategorySelectInterface selectInterface) {
        this.activity = activity;
        this.listData = listData;
        this.selectInterface = selectInterface;
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listData.get(position).isSelct()) {
                        Intent intent = new Intent(activity, ReviewListActivity.class);
                        intent.putExtra("categoryId",listData.get(position).getIdcategory());
                        intent.putExtra("categoryName",listData.get(position).getCategoryName());
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.amin_slide_in_left, R.anim.amin_slide_out_right);
                    }
                    else {
                        CategoryChangeDialog plusDialog = new CategoryChangeDialog(activity, -1, "", selectInterface);
                    }

                }
            });
            // item click method

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listData.get(getAdapterPosition()).isSelct()) {
                        if(listData.get(getAdapterPosition()).getIdcategory()<5) {
                            Toast.makeText(activity, "기본카테고리 항목을 수정/삭제 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                        CategorySelectDialog selectDialog = new CategorySelectDialog(activity, listData.get(getAdapterPosition()).getIdcategory(), listData.get(getAdapterPosition()).getCategoryName(), selectInterface);
                    }
                    return true;
                }
                // 선택 다이어로그 시작
            });
        }

        void onBind(CategoryData data) {
            int position = getAdapterPosition();

            category.setText(data.getCategoryName());
            if(data.isSelct()) {
                category.setTextColor(activity.getResources().getColorStateList(R.color.colorWhite));
                layout.setBackgroundResource(R.drawable.main_category_exist_background);
            }
            else {
                category.setTextColor(activity.getResources().getColorStateList(R.color.colorWhiteOpacity));
                layout.setBackgroundResource(R.drawable.main_category_empty_background);
            }

        }
        // set views
    }
}
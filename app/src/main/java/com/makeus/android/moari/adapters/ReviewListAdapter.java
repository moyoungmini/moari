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
import com.makeus.android.moari.activities.ReviewNonEditActivity;
import com.makeus.android.moari.datas.CategoryData;
import com.makeus.android.moari.datas.CurationData;
import com.makeus.android.moari.datas.ReviewListData;
import com.makeus.android.moari.dialogs.CategoryChangeDialog;
import com.makeus.android.moari.dialogs.CategorySelectDialog;
import com.makeus.android.moari.interfaces.DialogCategorySelectInterface;

import java.util.ArrayList;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ItemViewHolder> {

    private Activity activity;
    private ArrayList<ReviewListData> listData = new ArrayList<>();
    private DialogCategorySelectInterface selectInterface;

    public ReviewListAdapter(Activity activity) {
        this.activity = activity;
        this.listData = listData;
        this.selectInterface = selectInterface;
    }
    // constructor

    public void addListData(ArrayList<ReviewListData> tmp) {
        for(int i=0;i<tmp.size();i++){
            listData.add(tmp.get(i));
        }
    }

    public ArrayList<ReviewListData> getListData() {
        return listData;
    }

    public void clearData() {
        this.listData.clear();
    }

    public void addData(ReviewListData data) {
        listData.add(data);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review_list, parent, false);
        int height = parent.getMeasuredWidth() / 3;
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
        private ImageView image, rank;
        private TextView title;

        ItemViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.review_list_picture_show_iv);
            rank = itemView.findViewById(R.id.review_list_grade_iv);
            title = itemView.findViewById(R.id.review_list_title_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Intent intent = new Intent(activity, ReviewNonEditActivity.class);
                    intent.putExtra("id", listData.get(position).idboard);
                    activity.startActivity(intent);
                    // 리뷰 상세 수정x인걸로 ㄱㄱ
                }
            });
            // item click method
        }

        void onBind(ReviewListData data) {
            int position = getAdapterPosition();

            Glide.with(activity)
                    .load(data.getImage().toString())
                    .fitCenter()
                    .into(image);

            double grade = data.getGrade();
            if(grade ==0) {
                rank.setImageResource(R.drawable.rate_white_00);
            }
            else if(grade == 0.5) {
                rank.setImageResource(R.drawable.rate_white_01);
            }
            else if(grade == 1) {
                rank.setImageResource(R.drawable.rate_white_10);
            }
            else if(grade == 1.5) {
                rank.setImageResource(R.drawable.rate_white_11);
            }
            else if(grade == 2) {
                rank.setImageResource(R.drawable.rate_white_20);
            }
            else if(grade == 2.5) {
                rank.setImageResource(R.drawable.rate_white_21);
            }
            else if(grade == 3) {
                rank.setImageResource(R.drawable.rate_white_30);
            }
            else if(grade == 3.5) {
                rank.setImageResource(R.drawable.rate_white_31);
            }
            else if(grade == 4) {
                rank.setImageResource(R.drawable.rate_white_40);
            }
            else if(grade == 4.5) {
                rank.setImageResource(R.drawable.rate_white_41);
            }
            else if(grade == 5) {
                rank.setImageResource(R.drawable.rate_white_50);
            }
            else {
                rank.setImageResource(R.drawable.rate_white_30);
            }

            title.setText(data.getTitle().toString());

        }
        // set views
    }
}
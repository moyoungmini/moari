package com.makeus.android.moari.adapters;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeus.android.moari.R;
import com.makeus.android.moari.activities.ReviewEditActivity;
import com.makeus.android.moari.activities.ReviewNonEditActivity;
import com.makeus.android.moari.datas.CurationData;

import java.util.ArrayList;

public class CurationAdapter extends RecyclerView.Adapter<CurationAdapter.ItemViewHolder> {

    private Activity activity;
    private ArrayList<CurationData> listData = new ArrayList<>();

    public CurationAdapter(Activity activity, ArrayList<CurationData> listData) {
        this.activity = activity;
        this.listData = listData;
    }
    // constructor


    public ArrayList<CurationData> getListData() {
        return listData;
    }

    public void clearData() {
        this.listData.clear();
    }

    public void  addData(CurationData data) {
        listData.add(data);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_curation, parent, false);
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
        private TextView title, content, oneLine;
        private ImageView image, rank, shadowImage;
        private LinearLayout layout, gravityLayout;
        private FrameLayout frameLayout;

        ItemViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.category_item_title_tv);
            content = itemView.findViewById(R.id.category_item_content_tv);
            oneLine = itemView.findViewById(R.id.category_item_oneline_tv);
            image = itemView.findViewById(R.id.curation_item_iv);
            rank = itemView.findViewById(R.id.category_item_rank_iv);
            layout = itemView.findViewById(R.id.category_item_layout);
            frameLayout = itemView.findViewById(R.id.curation_item_layout);
            gravityLayout = itemView.findViewById(R.id.curation_item_gravity_layout);
            shadowImage = itemView.findViewById(R.id.curation_item_shadow_iv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = listData.get(getAdapterPosition()).getIdboard();
                    Intent intent = new Intent(activity, ReviewNonEditActivity.class);
                    intent.putExtra("id", id);
//                    intent.putExtra("flag",1); // update
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.amin_slide_in_left, R.anim.amin_slide_out_right);

                }
            });

            frameLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    frameLayout.setLayoutParams(new LinearLayout.LayoutParams(frameLayout.getMeasuredWidth(), frameLayout.getMeasuredWidth()));
                    float margin = (frameLayout.getMeasuredWidth() - gravityLayout.getMeasuredHeight()) / 2;
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT
                    );
//                params.setMargins(0, (int)margin, 0, (int)margin);
                    gravityLayout.setLayoutParams(params);
                }
            });
            // item click method
        }

        void onBind(CurationData data) {
            int position = getAdapterPosition();


            title.setText(data.getTitle());
            content.setText(data.getContent());
            oneLine.setText(data.getReview());

            if(data.getImage() == null || data.getImage().equals("")) {
                Glide.with(activity)
                        .load(R.drawable.default_background_img)
                        .fitCenter()
                        .into(image);
                shadowImage.setVisibility(View.INVISIBLE);
            }
            else {
                Glide.with(activity)
                        .load(data.getImage())
                        .fitCenter()
                        .into(image);
                shadowImage.setVisibility(View.VISIBLE);
            }

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

            if(data.getCategoryType() ==1) {
                layout.setBackgroundResource(R.color.colorCuration1);
            }
            else if(data.getCategoryType() ==2) {
                layout.setBackgroundResource(R.color.colorCuration3);
            }
            else if(data.getCategoryType() ==3) {
                layout.setBackgroundResource(R.color.colorCuration2);
            }
            else if(data.getCategoryType() ==4) {
                layout.setBackgroundResource(R.color.colorCuration4);
            }
            // default
            else if(data.getCategoryType() ==5) {
                layout.setBackgroundResource(R.color.colorCuration5);
            }
            else {
                layout.setBackgroundResource(R.color.colorCurationDefault);
            }

        }
        // set views
    }
}
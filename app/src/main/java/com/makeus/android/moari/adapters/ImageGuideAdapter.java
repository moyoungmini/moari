package com.makeus.android.moari.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeus.android.moari.MoariApp;
import com.makeus.android.moari.R;
import com.makeus.android.moari.activities.ReviewListActivity;
import com.makeus.android.moari.datas.CategoryData;
import com.makeus.android.moari.datas.ImageGuideData;
import com.makeus.android.moari.dialogs.CategoryChangeDialog;
import com.makeus.android.moari.dialogs.CategorySelectDialog;
import com.makeus.android.moari.interfaces.DialogCategorySelectInterface;

import java.util.ArrayList;

public class ImageGuideAdapter extends RecyclerView.Adapter<ImageGuideAdapter.ItemViewHolder> {

    private ArrayList<ImageGuideData> listData;
    private Activity activity;
    private boolean isStart;

    public ImageGuideAdapter(Activity activity) {
        this.activity = activity;
        this.listData = new ArrayList<>();
        SharedPreferences pref = activity.getSharedPreferences(MoariApp.TAG, Context.MODE_PRIVATE);
        isStart = pref.getBoolean("guide_access", true);
    }
    // constructor


    public ArrayList<ImageGuideData> getListData() {
        return listData;
    }

    public void addData(ImageGuideData data) {
        listData.add(data);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_guide, parent, false);
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
        private ImageView image, btn;

        ItemViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_guide_item_iv);
            btn = itemView.findViewById(R.id.image_guide_item_start_btn);
        }

        void onBind(ImageGuideData data) {
            Glide.with(activity).load(data.getImage()).into(image);

            if (getAdapterPosition() != 5) {
                btn.setVisibility(View.INVISIBLE);
            } else {
                if (isStart) {
                    SharedPreferences pref = activity.getSharedPreferences(MoariApp.TAG, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("guide_access", false);
                    editor.commit();

                    btn.setVisibility(View.VISIBLE);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            activity.finish();
                        }
                    });
                } else {
                    btn.setVisibility(View.INVISIBLE);
                }
            }
        }
        // set views
    }
}
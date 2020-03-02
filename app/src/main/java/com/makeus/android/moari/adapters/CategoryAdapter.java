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

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ItemViewHolder> {

    private Activity activity;
    private ArrayList<CategoryData> listData = new ArrayList<>();

    public CategoryAdapter(Activity activity, ArrayList<CategoryData> listData) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
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
        TextView title, content, oneLine;

        ItemViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.category_item_title_tv);
            content = itemView.findViewById(R.id.category_item_content_tv);
            oneLine = itemView.findViewById(R.id.category_item_oneline_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });
            // item click method
        }

        void onBind(CategoryData data) {
            int position = getAdapterPosition();


            title.setText(data.getTitle());

        }
        // set views
    }
}
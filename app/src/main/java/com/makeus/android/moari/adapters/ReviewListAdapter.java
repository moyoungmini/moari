//package com.makeus.android.moari.adapters;
//
//import android.app.Activity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.makeus.android.moari.R;
//import com.makeus.android.moari.datas.CurationData;
//import com.makeus.android.moari.datas.ReviewListData;
//
//import java.util.ArrayList;
//
//public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ItemViewHolder> {
//
//    private Activity activity;
//    private ArrayList<ReviewListData> listData = new ArrayList<>();
//
//    public ReviewListAdapter(Activity activity, ArrayList<ReviewListData> listData) {
//        this.activity = activity;
//        this.listData = listData;
//    }
//    // constructor
//
//
//    public ArrayList<ReviewListData> getListData() {
//        return listData;
//    }
//
//    public void clearData() {
//        this.listData.clear();
//    }
//
//    public void  addData(ReviewListData data) {
//        listData.add(data);
//    }
//
//    @NonNull
//    @Override
//    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout., parent, false);
//        return new ItemViewHolder(view);
//    }
//
//
//    @Override
//    public void onBindViewHolder(@NonNull final ItemViewHolder holder, final int position) {
//        holder.onBind(listData.get(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        return listData.size();
//    }
//
//    class ItemViewHolder extends RecyclerView.ViewHolder {
//        TextView title, content, oneLine;
//
//        ItemViewHolder(View itemView) {
//            super(itemView);
//            title = itemView.findViewById(R.id.category_item_title_tv);
//            content = itemView.findViewById(R.id.category_item_content_tv);
//            oneLine = itemView.findViewById(R.id.category_item_oneline_tv);
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//
//                }
//            });
//            // item click method
//        }
//
//        void onBind(CurationData data) {
//            int position = getAdapterPosition();
//
//
//            title.setText(data.getTitle());
//
//        }
//        // set views
//    }
//}
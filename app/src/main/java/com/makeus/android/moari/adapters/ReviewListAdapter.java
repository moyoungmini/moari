package com.makeus.android.moari.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.makeus.android.moari.MoariApp;
import com.makeus.android.moari.R;
import com.makeus.android.moari.activities.ReviewListActivity;
import com.makeus.android.moari.activities.ReviewNonEditActivity;
import com.makeus.android.moari.datas.ReviewListData;
import com.makeus.android.moari.dialogs.ReviewEditExitDialog;
import com.makeus.android.moari.interfaces.DialogReviewExitInterface;
import com.makeus.android.moari.responses.BasicResponse;
import java.util.ArrayList;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import static com.makeus.android.moari.MoariApp.catchAllThrowable;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ItemViewHolder> {

    private int deleteId = -1;
    private ReviewListActivity activity;
    private ArrayList<ReviewListData> listData = new ArrayList<>();
    private DialogReviewExitInterface mDeleteInterface = new DialogReviewExitInterface() {
        @Override
        public void exit() {

        }

        @Override
        public void delete() {
            deleteReview(deleteId);
        }
    };

    public ReviewListAdapter(Activity activity) {
        this.activity = (ReviewListActivity) activity;
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
        int height = parent.getMeasuredWidth() / 2;
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
        private ImageView image, rank, shadow;
        private TextView title;

        ItemViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.review_list_picture_show_iv);
            rank = itemView.findViewById(R.id.review_list_grade_iv);
            title = itemView.findViewById(R.id.review_list_title_tv);
            shadow = itemView.findViewById(R.id.review_list_shadow_iv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Intent intent = new Intent(activity, ReviewNonEditActivity.class);
                    intent.putExtra("id", listData.get(position).idboard);
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.amin_slide_in_left, R.anim.amin_slide_out_right);
                    // 리뷰 상세 수정x인걸로 ㄱㄱ
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    deleteId = listData.get(getAdapterPosition()).idboard;
                    ReviewEditExitDialog dialog = new ReviewEditExitDialog(activity, 1, mDeleteInterface);
                    return true;
                }
            });
            // item click method
        }

        void onBind(ReviewListData data) {
            int position = getAdapterPosition();

            if (data.getImage() == null || data.getImage().equals("")) {
                Glide.with(activity)
                        .load(R.drawable.default_background_img)
                        .fitCenter()
                        .into(image);
                shadow.setVisibility(View.INVISIBLE);
            } else {
                Glide.with(activity)
                        .load(data.getImage())
                        .fitCenter()
                        .into(image);
                shadow.setVisibility(View.VISIBLE);
            }

            double grade = data.getGrade();
            if (grade == 0) {
                rank.setImageResource(R.drawable.rate_white_00);
            } else if (grade == 0.5) {
                rank.setImageResource(R.drawable.rate_white_01);
            } else if (grade == 1) {
                rank.setImageResource(R.drawable.rate_white_10);
            } else if (grade == 1.5) {
                rank.setImageResource(R.drawable.rate_white_11);
            } else if (grade == 2) {
                rank.setImageResource(R.drawable.rate_white_20);
            } else if (grade == 2.5) {
                rank.setImageResource(R.drawable.rate_white_21);
            } else if (grade == 3) {
                rank.setImageResource(R.drawable.rate_white_30);
            } else if (grade == 3.5) {
                rank.setImageResource(R.drawable.rate_white_31);
            } else if (grade == 4) {
                rank.setImageResource(R.drawable.rate_white_40);
            } else if (grade == 4.5) {
                rank.setImageResource(R.drawable.rate_white_41);
            } else if (grade == 5) {
                rank.setImageResource(R.drawable.rate_white_50);
            } else {
                rank.setImageResource(R.drawable.rate_white_30);
            }

            title.setText(data.getTitle());

        }
        // set views
    }

    public void deleteReview(int id) {
        MoariApp.getRetrofitMethod(activity.getApplicationContext()).deleteReview(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BasicResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        activity.mCompositeDisposable.add(d);
                        activity.showProgressDialog();
                    }

                    @Override
                    public void onNext(BasicResponse res) {

                        if (res.getCode() == 200) {
                            activity.mPage = 0;
                            activity.mNoMoreItem = false;
                            activity.mLoadLock = false;
                            activity.mCategoryAdapter.clearData();
                            activity.getReviewList();
                        } else {
                            Toast.makeText(activity, res.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(activity.getApplicationContext(), catchAllThrowable(activity.getApplicationContext(), e), Toast.LENGTH_SHORT).show();
                        activity.dismissProgressDialog();
                    }

                    @Override
                    public void onComplete() {
                        activity.dismissProgressDialog();
                    }
                });
    }
}
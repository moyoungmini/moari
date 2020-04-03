//Copyright 2017, Yalantis
//
//        Licensed under the Apache License, Version 2.0 (the "License");
//        you may not use this file except in compliance with the License.
//        You may obtain a copy of the License at
//
//        http://www.apache.org/licenses/LICENSE-2.0
//
//        Unless required by applicable law or agreed to in writing, software
//        distributed under the License is distributed on an "AS IS" BASIS,
//        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//        See the License for the specific language governing permissions and
//        limitations under the License.

package com.makeus.android.moari.activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.JsonObject;
import com.makeus.android.moari.MoariApp;
import com.makeus.android.moari.R;
import com.makeus.android.moari.datas.CategoryData;
import com.makeus.android.moari.datas.ReviewDetailData;
import com.makeus.android.moari.dialogs.LoadingDialog;
import com.makeus.android.moari.dialogs.ReviewCameraDialog;
import com.makeus.android.moari.dialogs.ReviewCategoryDialog;
import com.makeus.android.moari.dialogs.ReviewEditExitDialog;
import com.makeus.android.moari.dialogs.ReviewRatingDialog;
import com.makeus.android.moari.interfaces.DialogCameraInterface;
import com.makeus.android.moari.interfaces.DialogCategoryInterface;
import com.makeus.android.moari.interfaces.DialogRatingInterface;
import com.makeus.android.moari.interfaces.DialogReviewExitInterface;
import com.makeus.android.moari.responses.BasicResponse;
import com.makeus.android.moari.responses.CategoryResponse;
import com.makeus.android.moari.responses.ReviewDetailResponse;
import com.yalantis.ucrop.UCrop;
import org.json.JSONException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;
import static com.makeus.android.moari.MoariApp.MEDIA_TYPE_JSON;
import static com.makeus.android.moari.MoariApp.catchAllThrowable;

public class ReviewEditActivity extends SuperActivity implements View.OnClickListener {

    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}; // permission check variable
    private static final int MULTIPLE_PERMISSIONS = 101; //use callback function after permession check
    private static final int PICK_FROM_CAMERA = 1; // bring picture using camera
    private static final int PICK_FROM_ALBUM = 2; // bring album using camera
    private static final int CROP_FROM_CAMERA = 3; // using crop
    private static final int BEFORE_IMAGE = 4; // using crop
    private static final int AFTER_IMAGE = 5; // using crop
    private static final int AFTER_SEVER_UPLOAD = 6; // using crop
    private int mMode = BEFORE_IMAGE; // use in onActivityResult function for distinguishing flag
    private Activity activity;
    private Uri photoUri, savingUri; // first picture, crop picture
    private static final String TAG = MainActivity.class.getSimpleName();
    private int year, month, dates;
    private ImageView mIvPictureShow, mIvRating, mIvDelete, mIvShadow;
    private TextView mTvDate, mTvCategory, mTvMain;
    private EditText mEtTitle, mEtOneLine, mEtContent;
    private ArrayList<CategoryData> mCategoryList = new ArrayList<>();
    private FrameLayout framelayout;
    private LinearLayout gravitySetLayout;
    private String title, oneLine, content, editDate, imageUrl;
    private int categoryId;
    private float grade;
    private int reviewId, flag;
    private File tmpFile;

    private DialogCategoryInterface mCategoryInterface = new DialogCategoryInterface() {
        @Override
        public void selectCategory(int id, String name) {
            mTvCategory.setText(name);
            categoryId = id;
        }
    };
    private DialogRatingInterface mRatingInterface = new DialogRatingInterface() {
        @Override
        public void rating(float rate) {
            grade = rate;
            setRate(rate);
        }
    };

    public void setRate(float rate) {
        if (rate == 0) {
            mIvRating.setBackgroundResource(R.drawable.rate_white_00);
        } else if (rate == 0.5) {
            mIvRating.setBackgroundResource(R.drawable.rate_white_01);
        } else if (rate == 1) {
            mIvRating.setBackgroundResource(R.drawable.rate_white_10);
        } else if (rate == 1.5) {
            mIvRating.setBackgroundResource(R.drawable.rate_white_11);
        } else if (rate == 2) {
            mIvRating.setBackgroundResource(R.drawable.rate_white_20);
        } else if (rate == 2.5) {
            mIvRating.setBackgroundResource(R.drawable.rate_white_21);
        } else if (rate == 3) {
            mIvRating.setBackgroundResource(R.drawable.rate_white_30);
        } else if (rate == 3.5) {
            mIvRating.setBackgroundResource(R.drawable.rate_white_31);
        } else if (rate == 4) {
            mIvRating.setBackgroundResource(R.drawable.rate_white_40);
        } else if (rate == 4.5) {
            mIvRating.setBackgroundResource(R.drawable.rate_white_41);
        } else if (rate == 5) {
            mIvRating.setBackgroundResource(R.drawable.rate_white_50);
        }
    }

    private DialogCameraInterface mCameraInterface = new DialogCameraInterface() {
        @Override
        public void album() {
            goToAlbum();
        }

        @Override
        public void camera() {
            takePhoto();
        }
    };

    private DialogReviewExitInterface mExitInterface = new DialogReviewExitInterface() {
        @Override
        public void exit() {
            finish();
            overridePendingTransition(R.anim.amin_slide_in_up, R.anim.amin_slide_out_down);
        }

        @Override
        public void delete() {
            deleteReview(reviewId);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_edit);

        initViews(); // set default views
        checkPermissions(); // permission check
        init();

        if (flag == 0) {
            mTvMain.setText("작성하기");
            Glide.with(activity)
                    .load(R.drawable.default_background_img)
                    .fitCenter()
                    .into(mIvPictureShow);
            mIvShadow.setVisibility(View.INVISIBLE);
            // set default image

            categoryId = getIntent().getIntExtra("categoryId", -1);
            getCategory();
        }
        // flag : 0 -> new edit

        else {
            mTvMain.setText("수정하기");
            getCategory();
            mIvShadow.setVisibility(View.VISIBLE);
        }
        // flag : 1 -> update edit
    }

    public void init() {
        framelayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                framelayout.setLayoutParams(new LinearLayout.LayoutParams(framelayout.getMeasuredWidth(), framelayout.getMeasuredWidth()));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                gravitySetLayout.setLayoutParams(params);
            }
        });
        // set layout width height to same because picture keep ratio shape

        reviewId = getIntent().getIntExtra("id", -1);
        flag = getIntent().getIntExtra("flag", 0);
        Calendar cal = Calendar.getInstance();
        year = cal.get(cal.YEAR);
        month = cal.get(cal.MONTH);
        dates = cal.get(cal.DATE);
        activity = this;
        grade = -1;
        categoryId = -1;
        imageUrl = "";
    }

    private boolean checkPermissions() {
        int result;
        List<String> permissionList = new ArrayList<>();
        for (String pm : permissions) {
            result = ContextCompat.checkSelfPermission(this, pm);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(pm);
            }
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
    // set permission

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        if (permissions[i].equals(this.permissions[0])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                Toast.makeText(this, R.string.permission_string, Toast.LENGTH_SHORT).show();
                            }
                        } else if (permissions[i].equals(this.permissions[1])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                Toast.makeText(this, R.string.permission_string, Toast.LENGTH_SHORT).show();
                            }
                        } else if (permissions[i].equals(this.permissions[2])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                Toast.makeText(this, R.string.permission_string, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, R.string.permission_string, Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
    // set onResult permission


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.review_edit_layout:
            case R.id.review_edit_picture_select_iv:
                if (mMode == BEFORE_IMAGE || mMode == AFTER_IMAGE) {
                    ReviewCameraDialog dialog = new ReviewCameraDialog(activity, mCameraInterface);
                } else if (mMode == AFTER_SEVER_UPLOAD) {
                    startActivity(new Intent(getApplication(), LoginActivity.class));
                    // I don't know
                }
                break;
            case R.id.review_edit_finish_iv:
                title = mEtTitle.getText().toString();
                content = mEtContent.getText().toString();
                oneLine = mEtOneLine.getText().toString();

                if (mTvDate.getText().equals("날짜")) {
                    Toast.makeText(activity, "날짜를 선택 해주세요", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (title.equals("")) {
                    Toast.makeText(activity, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (categoryId == -1) {
                    Toast.makeText(activity, "카테고리를 선택해주세요.", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (grade == -1) {
                    Toast.makeText(activity, "평점을 선택해주세요.", Toast.LENGTH_SHORT).show();
                    break;
                }

                editDate = mTvDate.getText().toString();
                editDate = editDate.replace(".", "-");
                editDate = editDate.replace(" ", "");

                if (savingUri == null) {
                    if (imageUrl == "" || imageUrl == null) {
                        imageUrl = "";
                    }
                    // exception handling
                    eidtReview();
                } else {
                    try {
                        try {
                            uploadFileToFireBase(savingUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
                }

                // make review
                break;
            case R.id.review_edit_date_tv:
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int tmpYear, int tmpMonth, int tmpDayOfMonth) {
                        year = tmpYear;
                        month = tmpMonth;
                        dates = tmpDayOfMonth;
                        String tmpStrMonth = "";
                        String tmpStrDate = "";
                        if (month + 1 < 10) {
                            tmpStrMonth = "0" + String.valueOf(month + 1);
                        } else {
                            tmpStrMonth = String.valueOf(month + 1);
                        }

                        if (dates < 10) {
                            tmpStrDate = "0" + String.valueOf(dates);
                        } else {
                            tmpStrDate = String.valueOf(dates);
                        }
                        String tmpDate = String.valueOf(year) + ". " + tmpStrMonth + ". " + tmpStrDate;
                        mTvDate.setText(tmpDate);
                    }
                }, year, month, dates);
                datePickerDialog.show();
                break;

            case R.id.review_edit_rating_layout:
                Log.i("RATING", String.valueOf(grade));
                ReviewRatingDialog dialog = new ReviewRatingDialog(activity, grade, mRatingInterface);
                break;
            case R.id.review_edit_category_tv:
                ReviewCategoryDialog categoryDialog = new ReviewCategoryDialog(activity, mTvCategory.getText().toString(), mCategoryList, mCategoryInterface);
                break;
            case R.id.review_edit_exit_iv:
                ReviewEditExitDialog exitDialog = new ReviewEditExitDialog(activity, 0, mExitInterface);
                break;
            case R.id.review_edit_delete_iv:
                ReviewEditExitDialog deleteDialog = new ReviewEditExitDialog(activity, 1, mExitInterface);
                break;
        }
    }

    @Override
    void initViews() {
        mTvMain = findViewById(R.id.rewview_edit_flag_tv);
        mIvPictureShow = findViewById(R.id.review_edit_picture_show_iv);
        mTvDate = findViewById(R.id.review_edit_date_tv);
        mTvCategory = findViewById(R.id.review_edit_category_tv);
        mEtTitle = findViewById(R.id.review_edit_title_et);
        mEtOneLine = findViewById(R.id.review_edit_line_et);
        mEtContent = findViewById(R.id.review_edit_content_et);
        mIvRating = findViewById(R.id.review_edit_rating_iv);
        mIvDelete = findViewById(R.id.review_edit_delete_iv);
        framelayout = findViewById(R.id.review_edit_layout);
        gravitySetLayout = findViewById(R.id.review_edit_gravity_layout);
        mIvShadow = findViewById(R.id.review_edit_shadow_iv);
    }

    private void takePhoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(activity, R.string.review_edit_image_error, Toast.LENGTH_SHORT).show();
            finish();
        }
        if (photoFile != null) {
            photoUri = FileProvider.getUriForFile(this,
                    "com.makeus.android.moari.provider", photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, PICK_FROM_CAMERA);
        }
    }
    // camera

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "IP" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/moari/");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        return image;
    }
    // create image(JPG) to moari file

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == 0) {
            return;
        }
        // when press back button in album

        if (requestCode == 1 && resultCode == 0) {
            return;
        }
        // when press back selecting camera

        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();

            if (tmpFile != null) {
                if (tmpFile.exists()) {
                    if (tmpFile.delete()) {
                        Log.e(TAG, tmpFile.getAbsolutePath() + " 삭제 성공");
                        tmpFile = null;
                    }
                }
            }
        }
        // exception handling

        if (requestCode == PICK_FROM_ALBUM) {
            if (data == null) {
                return;
            }
            photoUri = data.getData();
            cropImage();
        }
        // set uri in selecting album and start cropping
        else if (requestCode == PICK_FROM_CAMERA) {
            cropImage();
        }
        // set uri in camera and start cropping
        else if (requestCode == CROP_FROM_CAMERA) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), savingUri);
                Bitmap thumbImage = ThumbnailUtils.extractThumbnail(bitmap, 800, 800);
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                thumbImage.compress(Bitmap.CompressFormat.JPEG, 100, bs);
                // exract bitmap -> saving uri(crop image)

                mIvPictureShow.setImageBitmap(thumbImage);
                mIvShadow.setVisibility(View.VISIBLE);
                mMode = AFTER_IMAGE;
            } catch (Exception e) {
            }
        }
        // this code uses crop intent but not use this code. becuase crop intent can not use some API so uses below code

        else if (requestCode == UCrop.REQUEST_CROP) {
            try {
                Uri savingUri = UCrop.getOutput(data);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), savingUri);
                Bitmap thumbImage = ThumbnailUtils.extractThumbnail(bitmap, 800, 800);
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                thumbImage.compress(Bitmap.CompressFormat.JPEG, 100, bs);

                mIvPictureShow.setImageBitmap(thumbImage);
                mIvShadow.setVisibility(View.VISIBLE);
                mMode = AFTER_IMAGE;
            } catch (Exception e) {
            }
        }
        // use this code.
    }

    private void goToAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    public void cropImage() {
        if (tmpFile == null) {
            try {
                tmpFile = createImageFile();
            } catch (IOException e) {
                Toast.makeText(this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                finish();
                e.printStackTrace();
            }
        }

        savingUri = Uri.fromFile(tmpFile);

        UCrop.of(photoUri, savingUri)
                .withAspectRatio(16, 16)
                .start(activity);
        // important code

    }


    void uploadFileToFireBase(Uri mImageUri) throws JSONException, IOException {
        final LoadingDialog loadingDialog = new LoadingDialog(activity);
        showProgressDialog();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReferenceFromUrl("gs://moari-c6769.appspot.com/").child("android/" + mImageUri.getLastPathSegment());

        Bitmap bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), savingUri);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 15, baos); // 이걸로 줄이는거긴한데
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageRef.putBytes(data);


       uploadTask
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        dismissProgressDialog();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingDialog.dismiss();
                        Toast.makeText(activity, "업로드 실패", Toast.LENGTH_SHORT).show();
                    }
                })
                //진행중
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    }
                });

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return storageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();

                    imageUrl = downloadUri.toString();

                    Glide.with(activity)
                            .load(downloadUri.toString())
                            .fitCenter()
                            .into(mIvPictureShow);
                    mIvShadow.setVisibility(View.VISIBLE);
                    loadingDialog.dismiss();
                    mMode = AFTER_SEVER_UPLOAD;
                    eidtReview();
                } else {
                    //실패시
                }
            }
        });
    }

    public void getCategory() {
        MoariApp.getRetrofitMethod(getApplicationContext()).getCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CategoryResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                        showProgressDialog();
                    }

                    @Override
                    public void onNext(final CategoryResponse res) {
                        if (res.getCode() == 200) {
                            mCategoryList = res.getResult();

                            if (categoryId != -1) {
                                for (int i = 0; i < mCategoryList.size(); i++) {
                                    if (categoryId == mCategoryList.get(i).getIdcategory()) {
                                        mTvCategory.setText(mCategoryList.get(i).categoryName);
                                        break;
                                    }
                                }
                            }
                            // set categoryId and categoryName when receive categoryId
                            if(reviewId != -1) {
                                gerReviewDetail(reviewId);
                            }
                            // receive review data for sync. not use oncreate function

                        } else {
                            Toast.makeText(activity, res.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplicationContext(), catchAllThrowable(getApplicationContext(), e), Toast.LENGTH_SHORT).show();

                        dismissProgressDialog();
                    }

                    @Override
                    public void onComplete() {
                        dismissProgressDialog();
                    }
                });
    }

    public void postReview() {

        JsonObject params = new JsonObject();
        params.addProperty("title", title);
        params.addProperty("content", content);
        params.addProperty("image", imageUrl);
        params.addProperty("grade", grade);
        params.addProperty("review", oneLine);
        params.addProperty("reviewDate", editDate);

        MoariApp.getRetrofitMethod(getApplicationContext()).postReview(categoryId, RequestBody.create(MEDIA_TYPE_JSON, params.toString()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BasicResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                        showProgressDialog();
                    }

                    @Override
                    public void onNext(BasicResponse res) {

                        if (res.getCode() == 200) {
                            finish();
                            overridePendingTransition(R.anim.amin_slide_in_up, R.anim.amin_slide_out_down);
                        } else {
                            Toast.makeText(activity, res.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplicationContext(), catchAllThrowable(getApplicationContext(), e), Toast.LENGTH_SHORT).show();
                        dismissProgressDialog();
                    }

                    @Override
                    public void onComplete() {
                        dismissProgressDialog();
                    }
                });
    }

    public void updateReview() {
        JsonObject params = new JsonObject();
        params.addProperty("title", title);
        params.addProperty("content", content);
        params.addProperty("image", imageUrl);
        params.addProperty("grade", grade);
        params.addProperty("review", oneLine);
        params.addProperty("reviewDate", editDate);
        params.addProperty("categoryType", categoryId);

        MoariApp.getRetrofitMethod(getApplicationContext()).patchReview(reviewId, RequestBody.create(MEDIA_TYPE_JSON, params.toString()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BasicResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                        showProgressDialog();
                    }

                    @Override
                    public void onNext(BasicResponse res) {

                        if (res.getCode() == 200) {
                            finish();
                        } else {
                            Toast.makeText(activity, res.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplicationContext(), catchAllThrowable(getApplicationContext(), e), Toast.LENGTH_SHORT).show();
                        dismissProgressDialog();
                    }

                    @Override
                    public void onComplete() {
                        dismissProgressDialog();
                    }
                });
    }

    public void eidtReview() {
        if (reviewId == -1) {
            postReview();
        } else {
            updateReview();
        }
    }

    public void gerReviewDetail(int id) {
        MoariApp.getRetrofitMethod(getApplicationContext()).getReviewDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ReviewDetailResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                        showProgressDialog();
                    }

                    @Override
                    public void onNext(final ReviewDetailResponse res) {
                        if (res.getCode() == 200) {
                            if(res.getResult().size()<1) {
                                return;
                            }
                            ReviewDetailData data = res.getResult().get(0);
                            reviewId = data.getIdboard();
                            categoryId = data.getCategoryType();

                            String tmpDate = data.getReviewDate();
                            tmpDate = tmpDate.replace("-", ".");
                            tmpDate = tmpDate.replace(" ", "");
                            mTvDate.setText(tmpDate);
                            if (data.getReviewDate().length() >= 10) {
                                year = Integer.parseInt(data.getReviewDate().substring(0, 4));
                                month = Integer.parseInt(data.getReviewDate().substring(5, 7)) - 1;
                                dates = Integer.parseInt(data.getReviewDate().substring(8, 10));
                            }
                            // extract year, month, date in string text

                            for (int i = 0; i < mCategoryList.size(); i++) {
                                if (categoryId == mCategoryList.get(i).getIdcategory()) {
                                    mTvCategory.setText(mCategoryList.get(i).categoryName);
                                    break;
                                }
                            }
                            // compare categoryId and categoryName because server send categoryId

                            grade = data.getGrade();
                            setRate(grade);
                            mEtContent.setText(data.getContent());
                            mEtOneLine.setText(data.getReview());
                            mEtTitle.setText(data.getTitle());

                            Log.i("TESTTESFS", data.getImage());
                            if(data.getImage() != "" && data.getImage() != null) {
                                Glide.with(activity)
                                        .load(data.getImage())
                                        .fitCenter()
                                        .into(mIvPictureShow);
                            }
                            else {
                                Glide.with(activity)
                                        .load(R.drawable.default_background_img)
                                        .fitCenter()
                                        .into(mIvPictureShow);
                            }
                        } else {
                            Toast.makeText(activity, res.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplicationContext(), catchAllThrowable(getApplicationContext(), e), Toast.LENGTH_SHORT).show();

                        dismissProgressDialog();
                    }

                    @Override
                    public void onComplete() {
                        dismissProgressDialog();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        ReviewEditExitDialog exitDialog = new ReviewEditExitDialog(activity, 0, mExitInterface);
    }
    // start dialog pressing back button

    public void deleteReview(int id) {
        MoariApp.getRetrofitMethod(getApplicationContext()).deleteReview(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BasicResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                        showProgressDialog();
                    }

                    @Override
                    public void onNext(BasicResponse res) {

                        if (res.getCode() == 200) {
                            finish();
                        } else {
                            Toast.makeText(activity, res.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplicationContext(), catchAllThrowable(getApplicationContext(), e), Toast.LENGTH_SHORT).show();
                        dismissProgressDialog();
                    }

                    @Override
                    public void onComplete() {
                        dismissProgressDialog();
                    }
                });
    }
    // activity finish after review delete
}
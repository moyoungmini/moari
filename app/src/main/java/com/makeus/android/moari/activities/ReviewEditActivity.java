package com.makeus.android.moari.activities;


import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
    private Uri photoUri;
    public static final String FILE_NAME = "temp.jpg";
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int MAX_DIMENSION = 1200;
    private String url;
    private LoadingDialog loadingDialog;
    private int year, month, dates;
    private ImageView mIvPictureShow, mIvRating, mIvDelete;
    private TextView mTvDate, mTvCategory;
    private EditText mEtTitle, mEtOneLine, mEtContent;
    private ArrayList<CategoryData> mCategoryList = new ArrayList<>();

    private String postTitle, postOneLine, postContent, postDate, postUri;
    private int postId; // 카테고리 아이디
    private float postRating; // 별점
    private int updateId;

    private DialogCategoryInterface mCategoryInterface = new DialogCategoryInterface() {
        @Override
        public void selectCategory(int id, String name) {
            mTvCategory.setText(name);
            postId = id;
        }
    };
    private DialogRatingInterface mRatingInterface = new DialogRatingInterface() {
        @Override
        public void rating(float rate) {
            postRating = rate;
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
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_edit);

        initViews();
        checkPermissions();
        getCategory();

        int flag = getIntent().getIntExtra("flag", 0);
        if (flag == 0) {
            init();
        } else {
            updateInit();
        }
    }

    public void updateInit() {
        Calendar cal = Calendar.getInstance();
        year = cal.get(cal.YEAR);
        month = cal.get(cal.MONTH);
        dates = cal.get(cal.DATE);

//        mIvDelete.setVisibility(View.VISIBLE);

        loadingDialog = new LoadingDialog(this);
        activity = this;

        postRating = -1;
        postId = -1;

        postUri = "";

        int id = getIntent().getIntExtra("id", -1);
        gerReviewDetail(id);
    }

    public void init() {
        Calendar cal = Calendar.getInstance();
        year = cal.get(cal.YEAR);
        month = cal.get(cal.MONTH);
        dates = cal.get(cal.DATE);

        mIvDelete.setVisibility(View.INVISIBLE);

        loadingDialog = new LoadingDialog(this);
        activity = this;

        postRating = -1;
        postId = -1;

        postUri = "";

        Glide.with(activity)
                .load(R.drawable.default_background_img)
                .fitCenter()
                .into(mIvPictureShow);


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
            case R.id.review_edit_picture_select_iv:
                if (mMode == BEFORE_IMAGE || mMode == AFTER_IMAGE) {
                    ReviewCameraDialog dialog = new ReviewCameraDialog(activity, mCameraInterface);
                } else if (mMode == AFTER_SEVER_UPLOAD) {
                    startActivity(new Intent(getApplication(), LoginActivity.class));
                    // I don't know
                }
                break;
            case R.id.review_edit_finish_iv:
                postTitle = mEtTitle.getText().toString();
                postContent = mEtContent.getText().toString();
                postOneLine = mEtOneLine.getText().toString();

                if (mTvDate.getText().equals("날짜")) {
                    Toast.makeText(activity, "날짜를 선택 해주세요", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (postTitle.equals("")) {
                    Toast.makeText(activity, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (postId == -1) {
                    Toast.makeText(activity, "카테고리를 선택해주세요.", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (postRating == -1) {
                    Toast.makeText(activity, "평점을 선택해주세요.", Toast.LENGTH_SHORT).show();
                    break;
                }

                postDate = mTvDate.getText().toString();
                postDate = postDate.replace(".", "-");
                postDate = postDate.replace(" ", "");

                if (photoUri == null) {
                    if (postUri == "" || postUri == null) {
                        postUri = "";
                    }
                    eidtReview();
                } else {
                    try {
                        uploadFileToFireBase(photoUri);
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
                Log.i("RATING", String.valueOf(postRating));
                ReviewRatingDialog dialog = new ReviewRatingDialog(activity, postRating, mRatingInterface);
                break;
            case R.id.review_edit_category_tv:
                ReviewCategoryDialog categoryDialog = new ReviewCategoryDialog(activity, mCategoryList, mCategoryInterface);
                break;
            case R.id.review_edit_exit_iv:
                ReviewEditExitDialog exitDialog = new ReviewEditExitDialog(activity, mExitInterface);
                break;
        }
    }

    @Override
    void initViews() {
        mIvPictureShow = findViewById(R.id.review_edit_picture_show_iv);
        mTvDate = findViewById(R.id.review_edit_date_tv);
        mTvCategory = findViewById(R.id.review_edit_category_tv);
        mEtTitle = findViewById(R.id.review_edit_title_et);
        mEtOneLine = findViewById(R.id.review_edit_line_et);
        mEtContent = findViewById(R.id.review_edit_content_et);
        mIvRating = findViewById(R.id.review_edit_rating_iv);
        mIvDelete = findViewById(R.id.review_edit_delete_iv);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == 0) {
            return;
        }
        // 앨범선택 뒤로가기했을때 처리

        if (requestCode == 1 && resultCode == 0) {
            return;
        }
        // 카메라 선택후 뒤로가기했을때 처리


        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
        }
        if (requestCode == PICK_FROM_ALBUM) {
            if (data == null) {
                return;
            }
            photoUri = data.getData();
            cropImage();
        } else if (requestCode == PICK_FROM_CAMERA) {
            cropImage();
            MediaScannerConnection.scanFile(this, //앨범에 사진을 보여주기 위해 Scan을 합니다.
                    new String[]{photoUri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        } else if (requestCode == CROP_FROM_CAMERA) {
            try { //저는 bitmap 형태의 이미지로 가져오기 위해 아래와 같이 작업하였으며 Thumbnail을 추출하였습니다.

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                Bitmap thumbImage = ThumbnailUtils.extractThumbnail(bitmap, 800, 800);
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                thumbImage.compress(Bitmap.CompressFormat.JPEG, 100, bs); //이미지가 클 경우 OutOfMemoryException 발생이 예상되어 압축


                mIvPictureShow.setImageBitmap(thumbImage);
                mMode = AFTER_IMAGE;
            } catch (Exception e) {
            }
            Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
            if (data != null) {
                uploadImage(data.getData());
            }
        }
    }

    public File getCameraFile() {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);
    }

    private void goToAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK); //ACTION_PICK 즉 사진을 고르겠다!
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    public void uploadImage(Uri uri) {
        if (uri != null) {
            try {
                // scale the image to save on bandwidth
                Bitmap bitmap =
                        scaleBitmapDown(
                                MediaStore.Images.Media.getBitmap(getContentResolver(), uri),
                                MAX_DIMENSION);

                bitmap = rotate(bitmap, 90); //샘플이미지파일
            } catch (IOException e) {
                Log.d(TAG, "Image picking failed because " + e.getMessage());
                Toast.makeText(this, "다시 시도해주세요", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d(TAG, "Image picker gave us a null image.");
            Toast.makeText(this, "다시 시도해주세요", Toast.LENGTH_LONG).show();
        }
    }

    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public void cropImage() {
        this.grantUriPermission("com.android.camera", photoUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(photoUri, "image/*");

        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);
        grantUriPermission(list.get(0).activityInfo.packageName, photoUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        int size = list.size();
        if (size == 0) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Toast.makeText(this, "용량이 큰 사진의 경우 시간이 오래 걸릴 수 있습니다.", Toast.LENGTH_SHORT).show();
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            File croppedFileName = null;
            try {
                croppedFileName = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            File folder = new File(Environment.getExternalStorageDirectory() + "/inha/");
            File tempFile = new File(folder.toString(), croppedFileName.getName());

            photoUri = FileProvider.getUriForFile(this,
                    "com.makeus.android.moari.provider", tempFile);

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);


            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString()); //Bitmap 형태로 받기 위해 해당 작업 진행

            Intent i = new Intent(intent);
            ResolveInfo res = list.get(0);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            grantUriPermission(res.activityInfo.packageName, photoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            startActivityForResult(i, CROP_FROM_CAMERA);

        }

    }

    void uploadFileToFireBase(Uri mImageUri) throws JSONException {
        final LoadingDialog loadingDialog = new LoadingDialog(activity);
        showProgressDialog();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReferenceFromUrl("gs://fir-login-4684d.appspot.com/").child("user/" + mImageUri.getLastPathSegment());
        UploadTask uploadTask = storageRef.putFile(mImageUri);
        storageRef.putFile(mImageUri)
                //성공시
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        dismissProgressDialog();
//                        progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                    }
                })
                //실패시
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

                    postUri = downloadUri.toString();

                    Log.i("SDFSDF", postUri);
                    Glide.with(activity)
                            .load(downloadUri.toString())
                            .fitCenter()
                            .into(mIvPictureShow);

                    url = downloadUri.toString();
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

        JsonObject params = new JsonObject();
        params.addProperty("title", postTitle);
        params.addProperty("content", postContent);
        params.addProperty("image", postUri);
        params.addProperty("grade", postRating);
        params.addProperty("review", postOneLine);
        params.addProperty("reviewDate", postDate);

        MoariApp.getRetrofitMethod(getApplicationContext()).postReview(postId, RequestBody.create(MEDIA_TYPE_JSON, params.toString()))
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
                            ReviewDetailData data = res.getResult().get(0);
                            updateId = data.getIdboard();
                            postId = data.getCategoryType();
                            mTvDate.setText(data.getReviewDate());
                            if (data.getReviewDate().length() >= 10) {
                                year = Integer.parseInt(data.getReviewDate().substring(0, 4));
                                month = Integer.parseInt(data.getReviewDate().substring(5, 7))-1;
                                dates = Integer.parseInt(data.getReviewDate().substring(8, 10));
                                Log.i("TESF", String.valueOf(year));
                                Log.i("TESF", String.valueOf(month));
                                Log.i("TESF", String.valueOf(dates));
                            }
                            for (int i = 0; i < mCategoryList.size(); i++) {
                                Log.i("TESETET", mCategoryList.get(i).getCategoryName());
                                if (postId == mCategoryList.get(i).getIdcategory()) {
                                    mTvCategory.setText(mCategoryList.get(i).categoryName);
                                    break;
                                }
                            }
                            postRating = data.getGrade();
                            setRate(postRating);
                            mEtContent.setText(data.getContent());
                            mEtOneLine.setText(data.getReview());
                            mEtTitle.setText(data.getTitle());
                            postUri = data.getImage();
                            Glide.with(activity)
                                    .load(data.getImage())
                                    .fitCenter()
                                    .into(mIvPictureShow);

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
        ReviewEditExitDialog exitDialog = new ReviewEditExitDialog(activity, mExitInterface);
    }
}
package com.makeus.android.moari;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import java.util.concurrent.TimeUnit;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressLint("Registered")
public class MoariApp extends Application {

    // 싱글톤 데이터 리스트

    public static String TAG = "Moari_APP";
    public static String X_ACCESS_TOKEN = "JWT_TOKEN";

    public static Retrofit retrofit;

    public static MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json");
    public static MediaType MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg");


    public static String BASE_URL = "http://ec2-15-165-24-28.ap-northeast-2.compute.amazonaws.com:3000/";
    // 싱글톤 데이터 리스트
    @Override
    public void onCreate() {
        super.onCreate();

        //Tracking Start
        Stetho.initializeWithDefaults(this);

    }


    public static RetrofitInterface getRetrofitMethod(final Context context) {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .readTimeout(5000, TimeUnit.MILLISECONDS)
                    .connectTimeout(5000, TimeUnit.MILLISECONDS)
                    .addNetworkInterceptor(new StethoInterceptor())
                    .addNetworkInterceptor(new XAccessTokenInterceptor(context)) // JWT 자동 헤더 전송
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit.create(RetrofitInterface.class);
    }

//    public static String catchAllException(final Context context, final Exception exception) {
//        if (exception instanceof NullPointerException) {
//            return context.getString(R.string.error_null);
//        } else if (exception instanceof ConnectException) {
//            return context.getString(R.string.error_network);
//        } else {
//            return context.getString(R.string.error_default);
//            //개발할때 쓰던 코드
////            return exception.getMessage();
//        }
//    }



    public static String catchAllThrowable(final Context context, final Throwable throwable) {
        if (throwable instanceof NullPointerException) {
            return context.getString(R.string.error_null);
        } else {
            throwable.printStackTrace();
            //    Log.d("에러", throwable.toString());
            return context.getString(R.string.error_network);

        }

    }

}

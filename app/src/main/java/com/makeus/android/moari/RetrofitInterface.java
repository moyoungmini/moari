package com.makeus.android.moari;



import com.makeus.android.moari.responses.LoginResponse;
import com.makeus.android.moari.responses.SignupResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Retrofit에서 사용하는 통신 인터페이스
 */
public interface RetrofitInterface {

    @POST("app/signup")
    Observable<SignupResponse> postSignup(@Body RequestBody params);

    @POST("app/signin")
    Observable<LoginResponse> postLogin(@Body RequestBody params);



}

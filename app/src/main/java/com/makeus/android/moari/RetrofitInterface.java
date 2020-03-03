package com.makeus.android.moari;



import com.makeus.android.moari.responses.BasicResponse;
import com.makeus.android.moari.responses.CategoryResponse;
import com.makeus.android.moari.responses.LoginResponse;
import com.makeus.android.moari.responses.SignupResponse;
import com.makeus.android.moari.responses.UserResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
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

    @GET("user")
    Observable<UserResponse> getUser();

    @PATCH("user")
    Observable<BasicResponse> updateUser(@Body RequestBody params);

    @DELETE("user")
    Observable<BasicResponse> deleteUser();

    @GET("category")
    Observable<CategoryResponse> getCategory();

    @POST("category/{categoryId}/review")
    Observable<BasicResponse> postReview(@Path("categoryId") int categoryId, @Body RequestBody params);

}

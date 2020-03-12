package com.makeus.android.moari;



import com.makeus.android.moari.responses.BasicResponse;
import com.makeus.android.moari.responses.CategoryResponse;
import com.makeus.android.moari.responses.CurationResponse;
import com.makeus.android.moari.responses.LoginResponse;
import com.makeus.android.moari.responses.ReviewDetailResponse;
import com.makeus.android.moari.responses.ReviewListResponse;
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

    @POST("validation/user")
    Observable<BasicResponse> postValidationUser(@Body RequestBody params);

    @POST("find/user")
    Observable<BasicResponse> postFindUser(@Body RequestBody params);

    @GET("curation")
    Observable<CurationResponse> getCuration();

    @GET("review/{reviewId}")
    Observable<ReviewDetailResponse> getReviewDetail(@Path("reviewId") int reviewId);

    @POST("category")
    Observable<BasicResponse> plusCategory(@Body RequestBody params);

    @PATCH("category/{categoryId}")
    Observable<BasicResponse> changeCategory(@Path("categoryId") int categoryId, @Body RequestBody params);

    @DELETE("category/{categoryId}")
    Observable<BasicResponse> deleteCategory(@Path("categoryId") int categoryId);

    @GET("category/{categoryId}/review/{reviewId}")
    Observable<ReviewListResponse> getReviewList(@Path("categoryId") int categoryId, @Path("reviewId") int reviewId);

    @DELETE("review/{reviewId}")
    Observable<BasicResponse> deleteReview(@Path("reviewId") int reviewId);

    @PATCH("review/{reviewId}")
    Observable<BasicResponse> patchReview(@Path("reviewId") int reviewId, @Body RequestBody params);

    @GET("search/{page}")
    Observable<ReviewListResponse> getSearchList(@Path("page") int page,  @Body RequestBody params);
}

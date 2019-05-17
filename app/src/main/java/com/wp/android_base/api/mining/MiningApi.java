package com.wp.android_base.api.mining;

import com.wp.android_base.base.http.HttpResult;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * Created by wangpeng on 2018/7/12.
 */

public interface MiningApi {

    @POST("order/limit")
    Observable<HttpResult<LimitOrderData>> placeLimitOrder(@Header("authorization")String auth,@Body PlaceLimitOrderBody placeLimitOrderBody);

    @GET("market/depth")
    Observable<HttpResult<PriceData>> queryPrice(@Query("market")String market, @Query("limit")String limit, @Query("merge")String merge);

    @GET("order/")
    Observable<HttpResult<LimitOrderData>> queryOrderStatus(@Header("authorization")String auth,@Query("access_id")String access_id,@Query("id")String orderNumber,@Query("market")String market,@Query("tonce")String tonce);

    @DELETE("order/pending")
    Observable<HttpResult<LimitOrderData>> cancelOrder(@Header("authorization")String auth,@Query("access_id")String access_id,@Query("id")String orderNumber,@Query("market")String market,@Query("tonce")String tonce);


    @GET("order/mining/difficulty")
    Observable<HttpResult<DifficultyData>> getDifficulty(@Header("authorization")String auth,@Query("access_id")String access_id,@Query("tonce")String tonce);
}

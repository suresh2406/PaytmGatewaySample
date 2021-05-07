package com.suresh.paytmgatewaysample;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIInterface {
    @POST("theia/api/v1/initiateTransaction")
    Call<InitTransRes> getTransactionKey(@Body InitTrasnReq initTrasnReqBod, @Header("head") JSONObject head,
                                         @Query("mid") String merchantId, @Query("orderId") String orderId);
}

package com.adzerk.android.sdk.rest;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Service interface for Native Ads API
 */
public interface NativeAdService {

    @POST("/")
    void request(@Body Request request, Callback<Response> callback);

    @POST("/")
    Response request(@Body Request request);

}

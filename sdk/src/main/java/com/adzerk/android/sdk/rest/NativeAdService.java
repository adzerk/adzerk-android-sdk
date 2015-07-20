package com.adzerk.android.sdk.rest;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Service interface for Native Ads API
 */
public interface NativeAdService {

    // issues a request for ads
    @POST("/")
    void request(@Body Request request, Callback<Response> callback);
}

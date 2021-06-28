package com.adzerk.android.sdk.rest;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Service interface for Native Ads API
 */
public interface AdzerkService {

    /**
     * Request an Ad.
     * <p/>
     * NOTE: The placement in the Request corresponds to an ad placement on a page or app.
     */
    @POST("/api/v2")
    Call<DecisionResponse> request(@Body Request request);

    /**
     * Set the custom properties of a User by specifying properties in a JSON string
     * <p/>
     * Example: POST http://engine.adzerk.net/udb/{networkId}/custom?userKey=<user-key>
     */
    @POST("/udb/{networkId}/custom")
    Call<Void> postUserProperties(@Path("networkId") long networkId, @Query("userKey") String userKey, @Body RequestBody body);

    /**
     * Set the custom properties of a User by specifying properties using a Map object
     * <p/>
     * Example: POST http://engine.adzerk.net/udb/{networkId}/custom?userKey=<user-key>
     * <p/>
     * NOTE: Using this default content-type header fails with 400 Bad Request error:
     *       Content-Type: application/json; charset=UTF-8
     */
    @Headers( "Content-Type: application/json" )
    @POST("/udb/{networkId}/custom")
    Call<Void> postUserProperties(@Path("networkId") long networkId, @Query("userKey") String userKey, @Body Map<String, Object> body);

    /**
     * Reads a User from UserDB.
     * <p/>
     * The User object contains the users key, custom properties, interests and additional details.
     * <p/>
     * Example:  GET http://engine.adzerk.net/udb/{networkid}/read?userKey=<user-key>
     */
    @GET("/udb/{networkId}/read")
    Call<User> readUser(@Path("networkId") long networkId, @Query("userKey") String userKey);

    /**
     * Sets a User interest. The User object has a list of behavioral interest keywords (ie, cars, sports, ponies).
     * <p/>
     * Example: GET http://engine.adzerk.net/udb/{networkId}/interest/i.gif?userKey=<user-key>&interest=<interest>
     */
    @GET("/udb/{networkId}/interest/i.gif")
    Call<Void> setUserInterest(@Path("networkId") long networkId, @Query("userKey") String userKey, @Query("interest") String interest);

    /**
     * Sets a flag to allow User to opt-out of UserDB tracking. This call clears the entire user record and then
     * sets a value of "optedOut" to true.
     * <p/>
     * Example: GET http://engine.adzerk.net/udb/{networkid}/optout/i.gif?userKey=<user-key>
     */
    @GET("/udb/{networkId}/optout/i.gif")
    Call<Void> setUserOptout(@Path("networkId") long networkId, @Query("userKey") String userKey);

    /**
     * Retargeting creates a new number set called Segments-{brandId} that contains each of the segments.
     * <p/>
     * Example: GET http://engine.adzerk.net/udb/{networkId}/rt/{brandId}/{segment}/i.gif?userKey=<user-key>
     */
    @GET("/udb/{networkId}/rt/{brandId}/{segment}/i.gif")
    Call<Void> setUserRetargeting(@Path("networkId") long networkId,
                              @Path("brandId") long brandId,
                              @Path("segment") String segement,
                              @Query("userKey") String userKey);

    /**
     * Fire a click pixel
     * @param url
     * @param revenueOverride replaces the revenue value of the click/event
     * @param additionalRevenue adds the specified value to the original revenue value of the click/event
     * @return
     */
    @GET
    Call<Void> firePixel(@Url String url,
                             @Query("override") Float revenueOverride,
                             @Query("additional") Float additionalRevenue);
}

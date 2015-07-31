package com.adzerk.android.sdk.rest;

import java.util.Map;

import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedInput;

/**
 * Service interface for Native Ads API
 */
public interface NativeAdService {

    @POST("/api/v2")
    void request(@Body Request request, Callback<Response> callback);

    @POST("/api/v2")
    Response request(@Body Request request);

    /**
     * Set the custom properties of a User by specifying properties in a JSON string
     * <p/>
     * Example: POST http://engine.adzerk.net/udb/{networkId}/custom?userKey=<user-key>
     */
    @POST("/udb/{networkId}/custom")
    void postUserProperties(@Path("networkId") long networkId, @Query("userKey") String userKey, @Body TypedInput body, ResponseCallback callback);

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
    void postUserProperties(@Path("networkId") long networkId, @Query("userKey") String userKey, @Body Map<String, Object> body, ResponseCallback callback);

    /**
     * Reads a User from UserDB.
     * <p/>
     * The User object contains the users key, custom properties, interests and additional details.
     * <p/>
     * Example:  GET http://engine.adzerk.net/udb/{networkid}/read?userKey=<user-key>
     */
    @GET("/udb/{networkId}/read")
    void readUser(@Path("networkId") long networkId, @Query("userKey") String userKey, Callback<User> callback);

    /**
     * Sets a User interest. The User object has a list of behavioral interest keywords (ie, cars, sports, ponies).
     * <p/>
     * Example: GET http://engine.adzerk.net/udb/{networkId}/interest/i.gif?userKey=<user-key>&interest=<interest>
     */
    @GET("/udb/{networkId}/interest/i.gif")
    void setUserInterest(@Path("networkId") long networkId, @Query("userKey") String userKey, @Query("interest") String interest, ResponseCallback callback);

    /**
     * Opt-out
     *
     * We need to allow users to opt-out of userDB tracking.
     *
     * http://engine.adzerk.net/udb/{networkid}/optout/i.gif
     *
     * When this is called we should clear out their entire user record and then set a value of "optedOut" to true.
     * We then need to check this value before setting any data and if it's set we should abort the write. We should
     * also exclude this user from frequency capping.
     */

    /**
     * Retargeting
     *
     * It's a powerful feature for publishers to be able to sell retargeting for advertisers. This simple pixel will
     * create a new number set called Segments-{brandId} that contains each of the segments.
     *
     * add:
     *
     * http://engine.adzerk.net/udb/{networkId}/rt/{brandId}/{segment}/i.gif
     */

}

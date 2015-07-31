package com.adzerk.android.sdk.rest;

import java.util.Map;

import retrofit.Callback;
import retrofit.client.Response;
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
public interface AdzerkService {

    @POST("/api/v2")
    void request(@Body Request request, Callback<DecisionResponse> callback);

    @POST("/api/v2")
    DecisionResponse request(@Body Request request);

    /**
     * Set the custom properties of a User by specifying properties in a JSON string
     * <p/>
     * Example: POST http://engine.adzerk.net/udb/{networkId}/custom?userKey=<user-key>
     */
    @POST("/udb/{networkId}/custom")
    void postUserProperties(@Path("networkId") long networkId, @Query("userKey") String userKey, @Body TypedInput body, Callback<Response> cb);

    @POST("/udb/{networkId}/custom")
    Response postUserProperties(@Path("networkId") long networkId, @Query("userKey") String userKey, @Body TypedInput body);

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
    void postUserProperties(@Path("networkId") long networkId, @Query("userKey") String userKey, @Body Map<String, Object> body, Callback<Response> cb);

    @Headers( "Content-Type: application/json" )
    @POST("/udb/{networkId}/custom")
    Response postUserProperties(@Path("networkId") long networkId, @Query("userKey") String userKey, @Body Map<String, Object> body);

    /**
     * Reads a User from UserDB.
     * <p/>
     * The User object contains the users key, custom properties, interests and additional details.
     * <p/>
     * Example:  GET http://engine.adzerk.net/udb/{networkid}/read?userKey=<user-key>
     */
    @GET("/udb/{networkId}/read")
    void readUser(@Path("networkId") long networkId, @Query("userKey") String userKey, Callback<User> callback);

    @GET("/udb/{networkId}/read")
    User readUser(@Path("networkId") long networkId, @Query("userKey") String userKey);

    /**
     * Sets a User interest. The User object has a list of behavioral interest keywords (ie, cars, sports, ponies).
     * <p/>
     * Example: GET http://engine.adzerk.net/udb/{networkId}/interest/i.gif?userKey=<user-key>&interest=<interest>
     */
    @GET("/udb/{networkId}/interest/i.gif")
    void setUserInterest(@Path("networkId") long networkId, @Query("userKey") String userKey, @Query("interest") String interest, Callback<Response> cb);

    @GET("/udb/{networkId}/interest/i.gif")
    Response setUserInterest(@Path("networkId") long networkId, @Query("userKey") String userKey, @Query("interest") String interest);

    /**
     * Sets a flag to allow User to opt-out of UserDB tracking. This call clears the entire user record and then
     * sets a value of "optedOut" to true.
     * <p/>
     * Example: GET http://engine.adzerk.net/udb/{networkid}/optout/i.gif?userKey=<user-key>
     */
    @GET("/udb/{networkId}/optout/i.gif")
    void setUserOptout(@Path("networkId") long networkId, @Query("userKey") String userKey, Callback<Response> cb);

    @GET("/udb/{networkId}/optout/i.gif")
    Response setUserOptout(@Path("networkId") long networkId, @Query("userKey") String userKey);

    /**
     * Retargeting creates a new number set called Segments-{brandId} that contains each of the segments.
     * <p/>
     * Example: GET http://engine.adzerk.net/udb/{networkId}/rt/{brandId}/{segment}/i.gif?userKey=<user-key>
     */
    @GET("/udb/{networkId}/rt/{brandId}/{segment}/i.gif")
    void setUserRetargeting(@Path("networkId") long networkId,
                            @Path("brandId") long brandId,
                            @Path("segment") String segement,
                            @Query("userKey") String userKey,
                            Callback<Response> cb);

    @GET("/udb/{networkId}/rt/{brandId}/{segment}/i.gif")
    Response setUserRetargeting(@Path("networkId") long networkId,
                              @Path("brandId") long brandId,
                              @Path("segment") String segement,
                              @Query("userKey") String userKey);
}

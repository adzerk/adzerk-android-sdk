package com.adzerk.android.sdk;

import android.support.annotation.Nullable;
import android.util.Log;

import com.adzerk.android.sdk.rest.AdzerkService;
import com.adzerk.android.sdk.rest.ContentData;
import com.adzerk.android.sdk.rest.DecisionResponse;
import com.adzerk.android.sdk.rest.Request;
import com.adzerk.android.sdk.rest.User;
import com.adzerk.android.sdk.rest.UserProperties;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The Adzerk SDK provides the API for requesting native ads for your app.
 * <p>
 * To request Native Ad placement, you create and submit an ad Request using the SDK. Adzerk's ad engine will return decision
 * data and creative contents that can be used to serve ads in your application.
 * <p>
 * <pre>
 * {@code
 * // Get instance of the SDK
 * AdzerkSdk sdk = AdzerkSdk.getInstance();
 *
 * // Build the Request
 * Request request = new Request.Builder()
 *     .addPlacement(new Placement(name, networkId, siteId, adTypes))
 *     .build();
 *
 * // Issue the Request
 * sdk.requestPlacement(request, listener);
 * }
 * </pre>
 * @see com.adzerk.android.sdk.rest.Request.Builder
 */
public class AdzerkSdk {
    static final String TAG = AdzerkSdk.class.getSimpleName();
    static final String ADZERK_ENDPOINT = "https://engine.adzerk.net";

    static AdzerkSdk instance;

    AdzerkService service;
    OkHttpClient client;

    /**
     * Errors returned from Adzerk API calls.
     */
    public static class AdzerkError {
        int statusCode;
        String reason;

        public AdzerkError(int statusCode, String reason, Exception exception) {
            this.statusCode = statusCode;
            this.reason = reason;
        }

        public AdzerkError(Throwable t) {
            this.reason = t.getMessage();
        }

        public int getStatusCode() {
            return statusCode;
        }

        public String getReason() {
            return reason;
        }
    }

    private interface AdzerkCallbackListener<T> {
        public void success(T response);
        public void error(AdzerkError error);
    }

    /**
     * Listener for the DecisionResponse to an ad placement Request
     */
    public interface DecisionListener extends AdzerkCallbackListener<DecisionResponse> {
    }

    /**
     * Listener for the User response to a userDB request
     */
    public interface UserListener extends AdzerkCallbackListener<User> {
    }


    /**
     * Returns the SDK instance for making Adzerk API calls.
     *
     * @return sdk instance
     */
    public static AdzerkSdk getInstance() {
        if (instance == null) {
            instance = new AdzerkSdk();
        }

        return instance;
    }

    /**
     * Injection point for tests only. Not intended for public consumption.
     *
     * @param service service api
     * @return sdk instance
     */
    public static AdzerkSdk createInstance(AdzerkService service) {
        return new AdzerkSdk(service, null);
    }

    /**
     * Injection point for tests only. Not intended for public consumption.
     *
     * @param client - Inject http client
     * @return sdk instance
     */
    public static AdzerkSdk createInstance(OkHttpClient client) {
        return new AdzerkSdk(null, client);
    }

    private AdzerkSdk() {
        service = getAdzerkService();
    }

    private AdzerkSdk(AdzerkService service, OkHttpClient client) {
        this.service = service;
        this.client = client;
    }

    /**
     * Send a request to the Native Ads API.
     * This is an asynchronous request, results will be returned to the given listener.
     *
     * @param request ad Request specifying one or more Placements
     * @param listener Can be null, but caller will never get notifications.
     */
    public void requestPlacement(Request request, @Nullable final DecisionListener listener) {
        Call<DecisionResponse> call = getAdzerkService().request(request);
        call.enqueue(new AdzerkCallback<DecisionResponse, DecisionResponse>("RequestPlacement", listener));
    }

    /**
     * Send a synchronous request to the Native Ads API.
     *
     * @param request Request specifying one or more Placements
     */
    public DecisionResponse requestPlacementSynchronous(Request request) {
        Call<DecisionResponse> call = getAdzerkService().request(request);

        try {
            return  call.execute().body();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Set custom properties for User, specifying properties via JSON string.
     * <p/>
     * @param networkId unique network id
     * @param userKey   unique User key
     * @param json      a JSON String representing the custom properties, ie. { "age": 27, "gender": "male }
     * @param listener  callback listener, success arg is always null
     */
    public void setUserProperties(long networkId, String userKey, String json, @Nullable final UserListener listener) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
        Call<Void> call = getAdzerkService().postUserProperties(networkId, userKey, requestBody);
        call.enqueue(new AdzerkCallback<Void, User>("SetUserProperties", listener));
    }

    /**
     * Set custom properties for User, specifying properties via JSON string.
     * <p/>
     * @param networkId unique network id
     * @param userKey   unique User key
     * @param json      a JSON String representing the custom properties, ie. { "age": 27, "gender": "male }
     */
    public void setUserPropertiesSynchronous(long networkId, String userKey, String json) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
        getAdzerkService().postUserProperties(networkId, userKey, requestBody);
    }

    /**
     * Set custom properties for User, specifying properties via a Map object
     * <p/>
     * @param networkId     unique network id
     * @param userKey       unique User key
     * @param properties    map of key-value pairs
     * @param listener      callback listener
     */
    public void setUserProperties(long networkId, String userKey, Map<String, Object> properties, @Nullable final UserListener listener) {
        Call<Void> call = getAdzerkService().postUserProperties(networkId, userKey, properties);
        call.enqueue(new AdzerkCallback<Void, User>("SetUserProperties", listener));
    }

    /**
     * Set custom properties for User, specifying properties via a Map object
     * <p/>
     * @param networkId     unique network id
     * @param userKey       unique User key
     * @param properties    map of key-value pairs
     */
    public void setUserPropertiesSynchronous(long networkId, String userKey, Map<String, Object> properties) {
        getAdzerkService().postUserProperties(networkId, userKey, properties);
    }

    /**
     * Returns information about the User specified by userKey.
     * <p/>
     * @param networkId     unique network id
     * @param userKey       unique User key
     * @param listener      callback listener
     */
    public void readUser(long networkId, String userKey, @Nullable final UserListener listener) {
        Call<User> call =  getAdzerkService().readUser(networkId, userKey);
        call.enqueue(new AdzerkCallback<User, User>("SetUserProperties", listener));
    }

    /**
     * Returns information about the User specified by userKey.
     * <p/>
     * @param networkId     unique network id
     * @param userKey       unique User key
     * @return user object
     */
    public User readUserSynchronous(long networkId, String userKey) {
        Call<User> call = getAdzerkService().readUser(networkId, userKey);
        try {
            User user = call.execute().body();
            return  user;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Sets an interest for a User. The User object contains a list of user interest keywords.
     * <p/>
     * @param networkId     unique network id
     * @param userKey       unique User key
     * @param interest      name of interest
     * @param listener      callback listener
     */
    public void setUserInterest(long networkId, String userKey, String interest, @Nullable final UserListener listener) {
        Call<Void> call = getAdzerkService().setUserInterest(networkId, userKey, interest);
        call.enqueue(new AdzerkCallback<Void, User>("SetUserInterest", listener));
    }

    /**
     * Sets an interest for a User. The User object contains a list of user interest keywords.
     * <p/>
     * @param networkId     unique network id
     * @param userKey       unique User key
     * @param interest      name of interest
     */
    public void setUserInterestSynchronous(long networkId, String userKey, String interest) {
        getAdzerkService().setUserInterest(networkId, userKey, interest);
    }

    /**
     * Sets a flag to allow User to opt-out of tracking.
     * <p/>
     * @param networkId     unique network id
     * @param userKey       unique User key
     * @param listener      callback listener
     */
    public void setUserOptout(long networkId, String userKey, @Nullable final UserListener listener) {
        Call<Void> call = getAdzerkService().setUserOptout(networkId, userKey);
        call.enqueue(new AdzerkCallback<Void, User>("SetUserOptout", listener));
    }

    /**
     * Sets a flag to allow User to opt-out of tracking.
     * <p/>
     * @param networkId     unique network id
     * @param userKey       unique User key
     */
    public void setUserOptoutSynchronous(long networkId, String userKey) {
        getAdzerkService().setUserOptout(networkId, userKey);
    }

    /**
     * Sets ad retargeting for brand and segment.
     * <p/>
     * @param networkId     unique network id
     * @param brandId       unique brand id
     * @param segment       segment identifier
     * @param userKey       unique User key
     * @param listener      callback listener
     */
    public void setUserRetargeting(long networkId, long brandId, String segment, String userKey, @Nullable final UserListener listener) {
        Call<Void> call = getAdzerkService().setUserRetargeting(networkId, brandId, segment, userKey);
        call.enqueue(new AdzerkCallback<Void, User>("SetUserRetargeting", listener));
    }

    /**
     * Sets ad retargeting for brand and segment.
     * <p/>
     * @param networkId     unique network id
     * @param brandId       unique brand id
     * @param segment       segment identifier
     * @param userKey       unique User key
     */
    public void setUserRetargetingSynchronous(long networkId, long brandId, String segment, String userKey) {
        getAdzerkService().setUserRetargeting(networkId, brandId, segment, userKey);
    }

    /**
     * Converts the given String to an impression URL.
     *
     * @param urlString
     * @return - false if it is malformed
     */
    public boolean impression(final String urlString) {
        try {
            impression(new URL(urlString));
            return true;
        } catch (MalformedURLException e) {
            Log.e(TAG, "Failed to impress on url: " + urlString, e);
            return false;
        }
    }

    /**
     * Returns a request body for json string
     * @param jsonString
     * @return
     */
    public RequestBody createRequestBody(String jsonString) {
        return RequestBody.create(MediaType.parse("application/json"), jsonString);
    }

    protected void impression(final URL url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    url.openConnection().getContent();
                } catch(IOException e) {
                    Log.e(TAG, "Failed to impress ", e);
                }
            }
        }).start();
    }

    // Create service for the Adzerk REST endpoint
    private AdzerkService getAdzerkService() {
        if (service == null ) {
            Gson gson = new GsonBuilder()
                  .registerTypeAdapter(ContentData.class, new ContentDataDeserializer())
                  .registerTypeAdapter(UserProperties.class, new UserPropertiesDeserializer())
                  .setLenient()
                  .create();

            Retrofit.Builder builder = new Retrofit.Builder()
                  .baseUrl(ADZERK_ENDPOINT)
                  .addConverterFactory(GsonConverterFactory.create(gson));

            // test client
            if (client != null) {
                builder.client(client);
            } else {
                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
                //loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                httpClient.addInterceptor(loggingInterceptor);
                builder.client(httpClient.build());
            }

            service = builder.build().create(AdzerkService.class);
        }

        return service;
    }

    // Capture the default deserialization and JsonObject for the 'data.customData' element
    private static class ContentDataDeserializer implements JsonDeserializer<ContentData> {

        @Override
        public ContentData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject dataObject = json.getAsJsonObject();
            Map<String, Object> map = context.deserialize(dataObject, Map.class);
            JsonObject customDataObject = dataObject.getAsJsonObject("customData");

            return new ContentData(map, customDataObject);
        }
    }

    // Capture the default deserialization and JsonObject for the 'custom' element
    private static class UserPropertiesDeserializer implements JsonDeserializer<UserProperties> {

        @Override
        public UserProperties deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject dataObject = json.getAsJsonObject();
            Map<String, Object> map = context.deserialize(dataObject, Map.class);
            return new UserProperties(map, dataObject);
        }
    }

    private static String parseErrorBody(ResponseBody responseBody) {
        try {
            return responseBody.string();
        } catch (IOException e) {
            return null;
        }
    }

    public static class AdzerkCallback<T, R> implements Callback<T> {

        AdzerkCallbackListener<R> listener;
        String operation;

        public AdzerkCallback(String operation, AdzerkCallbackListener<R> listener) {
            System.out.println("adzerk callback created");
            this.operation = operation;
            this.listener = listener;
        }

        /**
         * Invoked for a received HTTP response.
         * <p>
         * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
         * Call {@link Response#isSuccessful()} to determine if the response indicates success.
         *
         * @param call
         * @param response
         */
        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            System.out.println("adzerk callback onResponse");

            if (listener == null) {
                return;
            }

            if (response.isSuccessful()) {
                T content = response.body();
                if (content == null || content instanceof Void) {
                    System.out.println("adzerk callback response is Void");
                    listener.success(null);
                } else {
                    System.out.println("adzerk callback response is " + content.getClass());
                    listener.success((R)content);
                }
            } else {
                int statusCode = response.code();
                String statusMessage = response.message();
                ResponseBody errorBody = response.errorBody();

                if (errorBody!=null) {
                    listener.error(new AdzerkError(statusCode, statusMessage, new Exception(operation + " failed: " + parseErrorBody(errorBody))));
                } else {
                    listener.error(new AdzerkError(statusCode, statusMessage, new Exception(operation + " failed: ")));
                }
            }
        }

        /**
         * Invoked when a network exception occurred talking to the server or when an unexpected
         * exception occurred creating the request or processing the response.
         *
         * @param call
         * @param t
         */
        @Override
        public void onFailure(Call<T> call, Throwable t) {
            if (listener != null) {
                listener.error(new AdzerkError(t));
            }
        }
    }
}

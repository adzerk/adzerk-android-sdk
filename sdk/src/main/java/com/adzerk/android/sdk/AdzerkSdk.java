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

import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.RestAdapter;
import retrofit.RestAdapter.Builder;
import retrofit.RestAdapter.LogLevel;
import retrofit.RetrofitError;
import retrofit.client.Client;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedString;

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
 * Request requestPlacement = new Request.Builder()
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
    Client client;

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

        public AdzerkError(RetrofitError error) {
            Response response = error.getResponse();
            if (response != null) {
                this.statusCode = response.getStatus();
                this.reason = response.getReason();
            }
        }
        public int getStatusCode() {
            return statusCode;
        }

        public String getReason() {
            return reason;
        }
    }

    /**
     * Listener for the DecisionResponse to an ad placement Request
     */
    public interface DecisionListener {
        public void success(DecisionResponse response);
        public void error(AdzerkError error);
    }

    /**
     * Listener for the User response to a userDB request
     */
    public interface UserListener {
        public void success(User user);
        public void error(AdzerkError error);
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
    public static AdzerkSdk createInstance(Client client) {
        return new AdzerkSdk(null, client);
    }

    private AdzerkSdk() {
        service = getAdzerkService();
    }

    private AdzerkSdk(AdzerkService service, Client client) {
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
        getAdzerkService().request(request, new Callback<DecisionResponse>() {
            @Override
            public void success(DecisionResponse response, Response response2) {
                if (listener != null) {
                    listener.success(response);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (listener != null) {
                    listener.error(new AdzerkError(error));
                }
            }
        });
    }

    /**
     * Send a synchronous request to the Native Ads API.
     *
     * @param request Request specifying one or more Placements
     */
    public DecisionResponse requestPlacementSynchronous(Request request) {
        return getAdzerkService().request(request);
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
        getAdzerkService().postUserProperties(networkId, userKey, new TypedJsonString(json), new ResponseCallback() {
            @Override
            public void success(Response response) {
                if (listener != null) {
                    listener.success(null);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (listener != null) {
                    listener.error(new AdzerkError(error));
                }
            }
        });
    }

    /**
     * Set custom properties for User, specifying properties via JSON string.
     * <p/>
     * @param networkId unique network id
     * @param userKey   unique User key
     * @param json      a JSON String representing the custom properties, ie. { "age": 27, "gender": "male }
     */
    public void setUserPropertiesSynchronous(long networkId, String userKey, String json) {
        getAdzerkService().postUserProperties(networkId, userKey, new TypedJsonString(json));
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
        getAdzerkService().postUserProperties(networkId, userKey, properties, new ResponseCallback() {
            @Override
            public void success(retrofit.client.Response response) {
                if (listener != null) {
                    listener.success(null);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (listener != null) {
                    listener.error(new AdzerkError(error));
                }
            }
        });
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
        getAdzerkService().readUser(networkId, userKey, new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                if (listener != null) {
                    listener.success(user);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (listener != null) {
                    listener.error(new AdzerkError(error));
                }
            }
        });
    }

    /**
     * Returns information about the User specified by userKey.
     * <p/>
     * @param networkId     unique network id
     * @param userKey       unique User key
     * @return user object
     */
    public User readUserSynchronous(long networkId, String userKey) {
        return getAdzerkService().readUser(networkId, userKey);
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
        getAdzerkService().setUserInterest(networkId, userKey, interest, new ResponseCallback() {
            @Override
            public void success(retrofit.client.Response response) {
                if (listener != null) {
                    listener.success(null);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (listener != null) {
                    listener.error(new AdzerkError(error));
                }
            }
        });
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
        getAdzerkService().setUserOptout(networkId, userKey, new ResponseCallback() {
            @Override
            public void success(retrofit.client.Response response) {
                if (listener != null) {
                    listener.success(null);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (listener != null) {
                    listener.error(new AdzerkError(error));
                }
            }
        });
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
        getAdzerkService().setUserRetargeting(networkId, brandId, segment, userKey, new ResponseCallback() {
            @Override
            public void success(retrofit.client.Response response) {
                if (listener != null) {
                    listener.success(null);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (listener != null) {
                    listener.error(new AdzerkError(error));
                }
            }
        });
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
     * Returns a typed json string to be serialized
     * @param jsonString
     * @return
     */
    public TypedJsonString createTypedJsonString(String jsonString) {
        return new TypedJsonString(jsonString);
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
                  .create();

            Builder builder = new RestAdapter.Builder()
                    .setEndpoint(ADZERK_ENDPOINT)
                    .setConverter(new GsonConverter(gson))
                    .setLogLevel(LogLevel.NONE);

            // test client
            if (client != null) {
                builder.setClient(client);
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

    private class TypedJsonString extends TypedString {
        public TypedJsonString(String body) {
            super(body);
        }

        @Override public String mimeType() {
            return "application/json";
        }
    }
}

package com.adzerk.android.sdk;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import com.adzerk.android.sdk.rest.NativeAdService;
import com.adzerk.android.sdk.rest.Request;
import com.adzerk.android.sdk.rest.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RestAdapter.Builder;
import retrofit.RetrofitError;
import retrofit.client.Client;
import retrofit.converter.GsonConverter;

/**
 * The Adzerk SDK provides the API for requesting native ads for you app.
 * <p>
 * To request Native Ads, you create and submit an ad Request using the SDK. Adzerk's ad engine will return decision
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
 * sdk.request(request, listener);
 * }
 * </pre>
 * @see com.adzerk.android.sdk.rest.Request.Builder
 */
public class AdzerkSdk {
    static final String TAG = AdzerkSdk.class.getSimpleName();

    static final String NATIVE_AD_ENDPOINT = "http://engine.adzerk.net/api/v2";

    static AdzerkSdk instance;

    NativeAdService service;
    Client client;

    /**
     * Listener for the Response to an ad Request
     */
    public interface ResponseListener {
        //TODO: Fine for a starting place, but we should use generic args so that we aren't
        //TODO: leaking retrofit abstractions through the sdk.
        public void success(Response response);
        public void error(RetrofitError error);
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
     * @param api service api
     * @return sdk instance
     */
    public static AdzerkSdk getInstance(NativeAdService api) {
        if (instance == null) {
            instance = new AdzerkSdk(api, null);
        }

        return instance;
    }

    /**
     * Injection point for tests only. Not intended for public consumption.
     *
     * @param client - Inject http client
     * @return sdk instance
     */
    public static AdzerkSdk getInstance(Client client) {
        if (instance == null) {
            instance = new AdzerkSdk(null, client);
        }

        return instance;
    }

    private AdzerkSdk() {
        service = getNativeAdsService();
    }

    private AdzerkSdk(NativeAdService service, Client client) {
        this.service = service;
        this.client = client;
    }

    /**
     * Send an ad request to the Native Ads API.
     *
     * @param request ad Request specifying one or more Placements
     * @param listener Can be null, but caller will never get notifications.
     */
    public void request(Request request, @Nullable final ResponseListener listener) {
        getNativeAdsService().request(request, new Callback<Response>() {
            @Override
            public void success(Response response, retrofit.client.Response response2) {
                if (listener != null) {
                    listener.success(response);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (listener != null) {
                    listener.error(error);
                }
            }
        });
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

    // Create service for the Native Ads API
    private NativeAdService getNativeAdsService() {
        if (service == null ) {
            Gson gson = new GsonBuilder().create();

            Builder builder = new RestAdapter.Builder()
                    .setEndpoint(NATIVE_AD_ENDPOINT)
                    .setConverter(new GsonConverter(gson))
                    .setLogLevel(RestAdapter.LogLevel.FULL);

            // test client
            if (client != null) {
                builder.setClient(client);
                builder.setExecutors(AsyncTask.THREAD_POOL_EXECUTOR, AsyncTask.THREAD_POOL_EXECUTOR);
            }

            service = builder.build().create(NativeAdService.class);
        }

        return service;
    }
}

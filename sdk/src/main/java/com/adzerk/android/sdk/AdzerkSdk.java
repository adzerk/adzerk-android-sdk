package com.adzerk.android.sdk;

import com.adzerk.android.sdk.rest.NativeAdsService;
import com.adzerk.android.sdk.rest.Request;
import com.adzerk.android.sdk.rest.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Adzerk SDK
 */
public class AdzerkSdk {

    private static AdzerkSdk sdk;

    private static NativeAdsService nativeAdsService;

    private String nativeAdsEndpoint = "http://engine.adzerk.net/api/v2";


    private AdzerkSdk() {
    }


    public interface ResponseListener {

        public void success(Response response);

        public void error(Error error);

    }


    /**
     * Returns the SDK instance for making Adzerk API calls
     *
     * @return
     */
    public static AdzerkSdk getInstance() {

        if (sdk == null) {
            sdk = new AdzerkSdk();
        }

        return sdk;
    }


    /**
     * Send an ad request to the Native Ads API
     *
     * @param request
     * @param listener
     */
    public void request(Request request, ResponseListener listener) {

        try {
            NativeAdsService service = getNativeAdsService();
            Response response = service.request(request);
            listener.success(response);
        } catch (Error error) {
            listener.error(error);
        }

    }


    /**
     * Override the default endpoint for Native Ads API
     *
     * @param nativeAdsEndpoint
     */
    public void setNativeAdsEndpoint(String nativeAdsEndpoint) {
        this.nativeAdsEndpoint = nativeAdsEndpoint;
        nativeAdsService = null;
    }


    // Create service for the Native Ads API
    private NativeAdsService getNativeAdsService() {

        if (nativeAdsService == null ) {

            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            RestAdapter restAdapter = new RestAdapter.Builder()
                  .setEndpoint(nativeAdsEndpoint)
                   //.setClient(new OkClient(new OkHttpClient()))
                  .setConverter(new GsonConverter(gson))
                  .setLogLevel(RestAdapter.LogLevel.FULL)
                   //.setLogLevel(RestAdapter.LogLevel.BASIC)
                  .build();

            nativeAdsService = restAdapter.create(NativeAdsService.class);
        }

        return nativeAdsService;
    }
}

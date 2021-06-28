package com.adzerk.android.sdk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Simple mock client that intercepts http request and replies with provided response.
 */
public class MockClient {
    int statusCode = 200;
    String reason = "OK";
    String responseString;
    Map<String, String> headers = new HashMap();

    public MockClient(String responseString) {
        this.responseString = responseString;
    }

    // Force an error response
    public void setResponseCode(int statusCode, String reason) {
        this.statusCode = statusCode;
        this.reason = reason;
    }

    public void addResponseHeader(String header, String value) {
        headers.put(header, value);
    }

    public OkHttpClient buildClient() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.addInterceptor(new MockResponseInterceptor());
        return builder.build();
    }

    private class MockResponseInterceptor implements Interceptor {

        private final MediaType MEDIA_JSON = MediaType.parse("application/json");

        @Override
        public Response intercept(Chain chain) throws IOException {
            Response mockResponse = null;

            Response.Builder builder = new Response.Builder()
                  .body(ResponseBody.create(MEDIA_JSON, responseString))
                  .request(chain.request())
                  .protocol(Protocol.HTTP_2)
                  .code(statusCode)
                  .message(reason);

            for (String header : headers.keySet()) {
                builder.addHeader(header, headers.get(header));
            }

            mockResponse = builder.build();

            return mockResponse;
        }
    }
}

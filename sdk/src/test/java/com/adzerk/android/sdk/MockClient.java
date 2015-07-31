package com.adzerk.android.sdk;

import java.io.IOException;
import java.util.Collections;

import retrofit.RetrofitError;
import retrofit.client.Client;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class MockClient implements Client {
    int statusCode = 200;
    String reason = "OK";
    String responseString;

    public MockClient(String responseString) {
        this.responseString = responseString;
    }

    // Force an error response
    public void setResponseCode(int statusCode, String reason) {
        this.statusCode = statusCode;
        this.reason = reason;
    }

    @Override
    public Response execute(Request request) throws IOException {
        if (statusCode < 299) {
            return new Response(request.getUrl(),
                    statusCode,
                    reason,
                    Collections.EMPTY_LIST,
                    new TypedByteArray("application/json", responseString.getBytes()));
        }

        throw RetrofitError.networkError("", new IOException());
    }
}

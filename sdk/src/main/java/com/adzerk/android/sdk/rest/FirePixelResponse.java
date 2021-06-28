package com.adzerk.android.sdk.rest;

/**
 * FirePixelResponse to an SDK fire pixel request
 */
public class FirePixelResponse {

    int statusCode;
    String location;

    public FirePixelResponse(int statusCode, String location) {
        this.statusCode = statusCode;
        this.location = location;
    }

    /**
     * HTTP status code
     * @return integer status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * The click target URL
     * @return url string
     */
    public String getLocation() {
        return this.location;
    }
}

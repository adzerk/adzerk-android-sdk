package com.adzerk.android.sdk;

import android.test.AndroidTestCase;
import android.util.Log;

import com.adzerk.android.sdk.rest.Placement;
import com.adzerk.android.sdk.rest.Request;
import com.adzerk.android.sdk.rest.Response;
import com.adzerk.androidsdk.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class NativeAdsTest extends AndroidTestCase {

    public NativeAdsTest() {
        super();
    }

    public void testAdRequest() {

        AdzerkSdk sdk = AdzerkSdk.getInstance();
        sdk.setNativeAdsEndpoint(getContext().getString(R.string.native_ads_endpoint));

        Request request = createTestRequest();
        sdk.request(request, new AdzerkSdk.ResponseListener() {

            @Override
            public void success(Response response) {

                // response should contain a decision for 'div1'
                Assert.assertNotNull(response);
                Assert.assertNotNull(response.getDecision("div1"));

                // show response for now ...
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                Log.d("NativeAdsTest", "Response JSON: " + gson.toJson(response));
            }

            @Override
            public void error(Error error) {
                Assert.fail("Request failed: " + error.getMessage());
            }
        });
    }

    private static Request createTestRequest() {

        // parameters
        String divName = "div1";
        long networkId = 9709;
        long siteId = 70464;
        String keyword = "karnowski";

        // Ad types to request for placement
        List<Integer> adTypes = new ArrayList<Integer>();
        adTypes.add(5);

        // create list of Placements
        List<Placement> placements = new ArrayList<Placement>();
        placements.add(new Placement(divName, networkId, siteId, adTypes));

        Request request = new Request.Builder(placements)
              .addKeyword(keyword)
              .build();

        return request;
    }
}

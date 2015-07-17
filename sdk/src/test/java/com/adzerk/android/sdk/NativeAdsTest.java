package com.adzerk.android.sdk;

import com.adzerk.android.sdk.rest.Placement;
import com.adzerk.android.sdk.rest.Request;
import com.adzerk.android.sdk.rest.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(sdk=18, constants=BuildConfig.class, manifest = Config.NONE)
public class NativeAdsTest {

    AdzerkSdk sdk;

    @Before
    public void setup() {
        sdk = AdzerkSdk.getInstance();
    }

    @Test
    public void itShouldRequestNativeAds() {

        final Request request = createTestRequest();
        sdk.request(request, new AdzerkSdk.ResponseListener() {

            @Override
            public void success(Response response) {

                // response should contain a decision for 'div1'
                assertNotNull(response);
                assertNotNull(response.getDecision(request.getPlacements().get(0).getDivName()));

                // log response json ...
                //Gson gson = new GsonBuilder().setPrettyPrinting().create();
                //Log.d("NativeAdsTest", "Response JSON: " + gson.toJson(response));
            }

            @Override
            public void error(Error error) {
                fail("Request failed. Error: " + error.getMessage());
            }
        });
    }

    private Request createTestRequest() {

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

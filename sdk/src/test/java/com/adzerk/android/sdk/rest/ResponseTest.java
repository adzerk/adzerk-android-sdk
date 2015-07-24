package com.adzerk.android.sdk.rest;

import com.adzerk.android.sdk.AdzerkSdk;
import com.adzerk.android.sdk.AdzerkSdk.ResponseListener;
import com.adzerk.android.sdk.BuildConfig;
import com.adzerk.android.sdk.MockClient;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import retrofit.RetrofitError;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(sdk=18, constants=BuildConfig.class, manifest = Config.NONE)
public class ResponseTest {

    AdzerkSdk sdk;
    MockClient mockClient;

    @Before
    public void setUp() throws Exception {
        mockClient = new MockClient(getMockJsonResponse());
        sdk = AdzerkSdk.getInstance(new MockClient(getMockJsonResponse()));
    }

    @Test
    public void itShouldDeserializeDecisions() {
        sdk.request(createTestRequest(), new ResponseListener() {
            @Override
            public void success(Response response) {
                assertThat(response.getDecisions()).containsOnlyKeys("div1", "div2");
                assertThat(response.getDecision("div2")).isNull();

                Decision div1 = response.getDecision("div1");
                assertThat(div1.getAdId()).isEqualTo(111);
                assertThat(div1.getCreativeId()).isEqualTo(222);
                assertThat(div1.getFlightId()).isEqualTo(333);
                assertThat(div1.getCampaignId()).isEqualTo(444);
                assertThat(div1.getClickUrl()).contains("click");
                assertThat(div1.getImpressionUrl()).contains("impression");
            }

            @Override
            public void error(RetrofitError error) {
                fail(error.getMessage());
            }
        });
    }

    @Test
    public void itShouldDeserializeContents() {
        sdk.request(createTestRequest(), new ResponseListener() {
            @Override
            public void success(Response response) {
                Content content = response.getDecision("div1").getContents().get(0);
                assertThat(content).isNotNull();
                assertThat(content.getData())
                        .isNotNull()
                        .isNotEmpty();
            }

            @Override
            public void error(RetrofitError error) {
                fail(error.getMessage());
            }
        });
    }

    @Test
    public void itShouldHandleNetworkErrors() {
        mockClient.setResponseCode(500, "Internal server error");
        sdk.request(createTestRequest(), new ResponseListener() {
            @Override
            public void success(Response response) {
                fail("Should not report success");
            }

            @Override
            public void error(RetrofitError error) {
                assertThat(error.getResponse().getStatus()).isEqualTo(500);
                assertThat(error.getResponse().getReason()).isEqualTo("Internal server error");
            }
        });
    }

    private Request createTestRequest() {
        String divName = "div1";
        long networkId = 9709;
        long siteId = 70464;

        List<Placement> placements = Arrays.asList(new Placement(divName, networkId, siteId, 5));
        return new Request.Builder(placements).build();
    }

    private String getMockJsonResponse() {
        return "{" +
                "  \"user\": {" +
                "    \"key\": \"ad39231daeb043f2a9610414f08394b5\"" +
                "  }," +
                "  \"decisions\": {" +
                "    \"div1\": {" +
                "      \"adId\": 111," +
                "      \"creativeId\": 222," +
                "      \"flightId\": 333," +
                "      \"campaignId\": 444," +
                "      \"clickUrl\": \"http://engine.adzerk.net/clicked\"," +
                "      \"contents\": [" +
                "        {" +
                "          \"type\": \"html\"," +
                "          \"template\": \"image\"," +
                "          \"data\": {" +
                "            \"imageUrl\": \"http://static.adzerk.net/cat-eating-spaghetti.jpg\"," +
                "            \"title\": \"ZOMG LOOK AT THIS FRICKING CAT\"," +
                "            \"width\": 350," +
                "            \"height\": 350" +
                "          }," +
                "          \"body\": \"<a href='...'><img src='http://static.adzerk.net/cat-eating-spaghetti.jpg' title='ZOMG LOOK AT THIS FRICKING CAT' width=350 height=350></a>\"" +
                "        }" +
                "      ]," +
                "      \"impressionUrl\": \"http://engine.adzerk.net/impression\"" +
                "    }," +
                "    \"div2\": null" +
                "  }" +
                "}";
    }

}
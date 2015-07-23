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
public class ContentTest {

    AdzerkSdk sdk;
    MockClient mockClient;

    @Before
    public void setUp() throws Exception {
        mockClient = new MockClient(getMockJsonResponse());
        sdk = AdzerkSdk.getInstance(new MockClient(getMockJsonResponse()));
    }

    @Test
    public void itShouldDeserializeResponse() {
        sdk.request(createTestRequest(), new ResponseListener() {
            @Override
            public void success(Response response) {
                assertThat(response.getUser().getKey()).isEqualTo("ad39231daeb043f2a9610414f08394b5");
                assertThat(response.getDecisions()).containsKey("div1");

                Decision div1 = response.getDecision("div1");
                assertThat(div1.getAdId()).isEqualTo(111);
                assertThat(div1.getCreativeId()).isEqualTo(222);
                assertThat(div1.getFlightId()).isEqualTo(333);
                assertThat(div1.getCampaignId()).isEqualTo(444);
                assertThat(div1.getClickUrl()).contains("adzerk");
                assertThat(div1.getImpressionUrl()).contains("adzerk");

                List<Content> contents = div1.getContents();
                assertThat(contents.size()).isEqualTo(1);

                Content div1Content = contents.get(0);
                assertThat(div1Content.getType()).isEqualTo(Content.TYPE_HTML);
                assertThat(div1Content.getTemplate()).isEqualTo(Content.TEMPLATE_IMAGE);
                assertThat(div1Content.getData()).containsKeys("imageUrl", "title");
                assertThat(div1Content.getBody()).isNotEmpty();

                assertThat(div1Content.getCustomData()).isNotEmpty();
                assertThat(div1Content.getCustomData()).containsOnlyKeys("foo", "bar");
                assertThat(div1Content.getCustomData().get("foo")).isEqualTo(new Integer(42));
                assertThat(div1Content.getCustomData().get("foo")).isEqualTo("some string");

                assertThat(div1.getEvents()).isNotEmpty();
                assertThat(div1.getEvents()).hasSize(3);
                assertThat(div1.getEvents().get(0).getEventId()).isEqualTo(12);
                assertThat(div1.getEvents().get(0).getEventUrl()).contains("adzerk");
            }

            @Override
            public void error(RetrofitError error) {
                fail(error.getMessage());
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
        
        return  "{" +
              "  \"user\": {" +
              "    \"key\": \"ad39231daeb043f2a9610414f08394b5\"" +
              "  }," +
              "  \"decisions\": {" +
              "    \"div1\": {" +
              "      \"adId\": 111," +
              "      \"creativeId\": 222," +
              "      \"flightId\": 333," +
              "      \"campaignId\": 444," +
              "      \"clickUrl\": \"http://engine.adzerk.net/r?...\"," +
              "      \"contents\": [" +
              "      {" +
              "        \"type\": \"html\"," +
              "        \"template\": \"image\"," +
              "        \"data\": {" +
              "          \"imageUrl\": \"http://static.adzerk.net/cat-eating-spaghetti.jpg\"," +
              "          \"title\": \"ZOMG LOOK AT THIS FRICKING CAT\"," +
              "          \"width\": 300," +
              "          \"height\": 250" +
              "          \"customData\": { \"foo\": 42, \"bar\": \"some string\" }" +
              "        }," +
              "      \"body\": \"<a href='...'><img src='http://static.adzerk.net/cat-eating-spaghetti.jpg' title='ZOMG LOOK AT THIS FRICKING CAT' width=\"350\" height=\"350\"></a>\"" +
              "      }" +
              "    ]," +
              "    \"impressionUrl\": \"http://engine.adzerk.net/i.gif?...\"" +
              "    \"events\": [" +
              "        { eventId: 12," +
              "          eventUrl: \"http://engine.adzerk.net/e.gif?...\"" +
              "        }," +
              "        { eventId: 13," +
              "          eventUrl: \"http://engine.adzerk.net/e.gif?...\"" +
              "        }," +
              "        { eventId: 14," +
              "          eventUrl: \"http://engine.adzerk.net/e.gif?...\"" +
              "        }" +
              "      ]" +
              "    }" +
              "  }" +
              "}";
    }

}
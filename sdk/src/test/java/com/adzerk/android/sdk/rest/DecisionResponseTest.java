package com.adzerk.android.sdk.rest;

import com.adzerk.android.sdk.AdzerkSdk;
import com.adzerk.android.sdk.BuildConfig;
import com.adzerk.android.sdk.MockClient;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(sdk=25, constants=BuildConfig.class)
public class DecisionResponseTest {

    AdzerkSdk sdk;
    MockClient mockClient;

    @Before
    public void setUp() throws Exception {
        mockClient = new MockClient(JSON_DECISIONS);
        sdk = AdzerkSdk.createInstance(mockClient.buildClient());
    }

    @Test
    public void itShouldDeserializeDecisions() {
        DecisionResponse response = sdk.requestPlacementSynchronous(createTestRequest());
        assertThat(response).isNotNull();

        // User
        assertThat(response.getUser().getKey()).isEqualTo("ad39231daeb043f2a9610414f08394b5");

        // Decisions
        assertThat(response.getDecisions()).containsKey("div1");
        assertThat(response.getDecisions()).containsKey("div2");
        assertThat(response.getDecision("div2")).isNull();

        Decision div1 = response.getDecision("div1");
        assertThat(div1.getAdId()).isEqualTo(111);
        assertThat(div1.getCreativeId()).isEqualTo(222);
        assertThat(div1.getFlightId()).isEqualTo(333);
        assertThat(div1.getCampaignId()).isEqualTo(444);
        assertThat(div1.getClickUrl()).contains("click");
        assertThat(div1.getImpressionUrl()).contains("impression");

        // Events
        assertThat(div1.getEvents())
                .isNotEmpty()
                .hasSize(3);

        assertThat(div1.getEvents().get(0).getId()).isEqualTo(12);
        assertThat(div1.getEvents().get(0).getUrl()).contains("adzerk");
    }

    @Test
    public void itShouldDeserializeContents() {
        DecisionResponse response = sdk.requestPlacementSynchronous(createTestRequest());
        assertThat(response).isNotNull();

        Content content = response.getDecision("div1").getContents().get(0);
        assertThat(content).isNotNull();
        assertThat(content.hasCreativeData()).isTrue();
        assertThat(content.isImage()).isTrue();
        assertThat(content.getImageUrl()).isEqualTo("http://static.adzerk.net/cat-eating-spaghetti.jpg");
    }

    private Request createTestRequest() {
        return new Request.Builder()
                .addPlacement(new Placement("div1", 9709L, 70464L, 5))
                .build();
    }

    static final String JSON_DECISIONS = "{" +
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
        "       {" +
        "           \"type\": \"html\"," +
        "           \"template\": \"image\"," +
        "           \"data\": {" +
        "               \"imageUrl\": \"http://static.adzerk.net/cat-eating-spaghetti.jpg\"," +
        "               \"title\": \"ZOMG LOOK AT THIS FRICKING CAT\"," +
        "               \"width\": 300," +
        "               \"height\": 250," +
        "               \"customData\": { \"foo\": 42, \"bar\": \"some string\" }" +
        "           }," +
        "           \"body\": \"<a href='...'><img src='http://static.adzerk.net/cat-eating-spaghetti.jpg' title='ZOMG LOOK AT THIS FRICKING CAT' width=350 height=350></a>\"" +
        "       }" +
        "      ]," +
        "       \"impressionUrl\": \"http://engine.adzerk.net/impression\", " +
        "       \"events\": [" +
        "        { id: 12," +
        "          url: \"http://engine.adzerk.net/e.gif?...\"" +
        "        }," +
        "        { id: 13," +
        "          url: \"http://engine.adzerk.net/e.gif?...\"" +
        "        }," +
        "        { id: 14," +
        "          url: \"http://engine.adzerk.net/e.gif?...\"" +
        "        }" +
        "       ]" +
        "    }," +
        "    \"div2\": null" +
        "  }" +
        "}";

}
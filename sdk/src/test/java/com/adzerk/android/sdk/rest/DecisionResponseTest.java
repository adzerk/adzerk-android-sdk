package com.adzerk.android.sdk.rest;

import android.location.Location;

import com.adzerk.android.sdk.AdzerkSdk;
import com.adzerk.android.sdk.MockClient;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(sdk=35)
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
        assertThat(response.getDecisions("div2")).isNull();

        Decision div1 = response.getDecisions("div1").get(0);
        assertThat(div1.getAdId()).isEqualTo(111);
        assertThat(div1.getCreativeId()).isEqualTo(222);
        assertThat(div1.getFlightId()).isEqualTo(333);
        assertThat(div1.getCampaignId()).isEqualTo(444);
        assertThat(div1.getAdvertiserId()).isEqualTo(555);
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

        Content content = response.getDecisions("div1").get(0).getContents().get(0);
        assertThat(content).isNotNull();
        assertThat(content.hasCreativeData()).isTrue();
        assertThat(content.isImage()).isTrue();
        assertThat(content.getImageUrl()).isEqualTo("http://static.adzerk.net/cat-eating-spaghetti.jpg");
    }

    @Test
    public void itShouldDeserializeMultiWinners() {
        AdzerkSdk sdk = AdzerkSdk.createInstance(new MockClient(JSON_MULTI_WINNERS).buildClient());

        DecisionResponse response = sdk.requestPlacementSynchronous(createTestRequest());
        assertThat(response).isNotNull();

        // User
        assertThat(response.getUser().getKey()).isEqualTo("ue1-a58d96713f6a41edb42695d24178e224");

        // Decisions
        assertThat(response.getDecisions()).containsKey("div1");

        List<Decision> div1Decisions = response.getDecisions("div1");
        assertThat(div1Decisions).isNotNull().isNotEmpty().hasSize(3);
    }

    @Test
    public void itShouldDeserializeMatchedPoints() {
        AdzerkSdk sdk = AdzerkSdk.createInstance(new MockClient(JSON_DECISION_WITH_MATCHEDPOINTS).buildClient());

        DecisionResponse response = sdk.requestPlacementSynchronous(createTestRequest());
        assertThat(response).isNotNull();

        // Decisions
        assertThat(response.getDecisions()).containsKey("div1");

        List<Decision> div1Decisions = response.getDecisions("div1");
        assertThat(div1Decisions).isNotNull().isNotEmpty().hasSize(1);
        List<Location> matchedPoints = div1Decisions.get(0).getMatchedPoints();
        assertThat(matchedPoints).isNotNull().isNotEmpty().hasSize(3);
    }

    @Test
    public void itShouldDeserializeNumericMatchedPoints() {
        AdzerkSdk sdk = AdzerkSdk.createInstance(new MockClient(JSON_DECISION_WITH_NEW_FIELDS).buildClient());

        DecisionResponse response = sdk.requestPlacementSynchronous(createTestRequest());
        assertThat(response).isNotNull();

        List<Location> matchedPoints = response.getDecisions("div1").get(0).getMatchedPoints();
        assertThat(matchedPoints).isNotNull().isNotEmpty().hasSize(2);
        assertThat(matchedPoints.get(0).getLatitude()).isEqualTo(35.995063);
        assertThat(matchedPoints.get(0).getLongitude()).isEqualTo(-78.908187);
        assertThat(matchedPoints.get(1).getLatitude()).isEqualTo(40.689188);
        assertThat(matchedPoints.get(1).getLongitude()).isEqualTo(-74.044562);
    }

    @Test
    public void itShouldDeserializeDimensions() {
        AdzerkSdk sdk = AdzerkSdk.createInstance(new MockClient(JSON_DECISION_WITH_NEW_FIELDS).buildClient());

        DecisionResponse response = sdk.requestPlacementSynchronous(createTestRequest());
        Decision div1 = response.getDecisions("div1").get(0);

        assertThat(div1.getHeight()).isEqualTo(250);
        assertThat(div1.getWidth()).isEqualTo(300);
    }

    @Test
    public void itShouldDeserializeExternalMetadata() {
        AdzerkSdk sdk = AdzerkSdk.createInstance(new MockClient(JSON_DECISION_WITH_NEW_FIELDS).buildClient());

        DecisionResponse response = sdk.requestPlacementSynchronous(createTestRequest());
        Decision div1 = response.getDecisions("div1").get(0);

        assertThat(div1.getExternalMetadata()).isNotNull().hasSize(2);
        assertThat(div1.getExternalMetadata().get("campaignTag")).isEqualTo("summer-sale");
        assertThat(div1.getExternalMetadata().get("priority")).isEqualTo(3.0);
    }

    @Test
    public void itShouldDeserializeEcpmPartition() {
        AdzerkSdk sdk = AdzerkSdk.createInstance(new MockClient(JSON_DECISION_WITH_NEW_FIELDS).buildClient());

        DecisionResponse response = sdk.requestPlacementSynchronous(createTestRequest());

        assertThat(response.getDecisions("div1").get(0).getEcpmPartition()).isEqualTo("partition-a");
    }

    @Test
    public void itShouldDeserializeAdChain() {
        AdzerkSdk sdk = AdzerkSdk.createInstance(new MockClient(JSON_DECISION_WITH_NEW_FIELDS).buildClient());

        DecisionResponse response = sdk.requestPlacementSynchronous(createTestRequest());
        List<Decision> adChain = response.getDecisions("div1").get(0).getAdChain();

        assertThat(adChain).isNotNull().isNotEmpty().hasSize(1);
        assertThat(adChain.get(0).getAdId()).isEqualTo(999);
        assertThat(adChain.get(0).getCreativeId()).isEqualTo(888);
    }

    @Test
    public void itShouldDeserializePricingData() {
        AdzerkSdk sdk = AdzerkSdk.createInstance(new MockClient(JSON_DECISION_WITH_NEW_FIELDS).buildClient());

        DecisionResponse response = sdk.requestPlacementSynchronous(createTestRequest());
        PricingData pricing = response.getDecisions("div1").get(0).getPricing();

        assertThat(pricing).isNotNull();
        assertThat(pricing.getPrice()).isEqualTo(3.0f);
        assertThat(pricing.getClearPrice()).isEqualTo(2.0f);
        assertThat(pricing.getModifiedPrice()).isEqualTo(1.5f);
        assertThat(pricing.getOptimizedPrice()).isEqualTo(2.25f);
        assertThat(pricing.getEventMultiplier()).isEqualTo(1.1f);
        assertThat(pricing.getRevenue()).isEqualTo(0.003f);
        assertThat(pricing.getRateType()).isEqualTo(2);
        assertThat(pricing.getECPM()).isEqualTo(3.0f);
    }

    @Test
    public void itShouldLeaveNewFieldsNullWhenAbsent() {
        DecisionResponse response = sdk.requestPlacementSynchronous(createTestRequest());
        Decision div1 = response.getDecisions("div1").get(0);

        assertThat(div1.getHeight()).isNull();
        assertThat(div1.getWidth()).isNull();
        assertThat(div1.getExternalMetadata()).isNull();
        assertThat(div1.getEcpmPartition()).isNull();
        assertThat(div1.getAdChain()).isNull();
        assertThat(div1.getPricing()).isNull();
    }

    @Test
    public void itShouldFirePixelSuccessfully() {
        AdzerkSdk sdk = new AdzerkSdk.Builder().networkId(9792L).build();

        int[] adTypes  = {4,5};
        Request request = new Request.Builder()
                .addPlacement(new Placement("div1", 306998L, adTypes).setCount(3))
                .build();
        DecisionResponse response  = sdk.requestPlacementSynchronous(request);
        Decision firstDecision = response.getDecisions("div1").get(0);
        String pixelUrl = firstDecision.getClickUrl();
        FirePixelResponse fpr = sdk.firePixelSynchronous(pixelUrl, 1.25f, AdzerkSdk.RevenueModifierType.ADDITIONAL, null);

        assertThat(fpr.getStatusCode()).isEqualTo(302);
        assertThat(fpr.getLocation()).isEqualTo("http://adzerk.com");;
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
        "      \"advertiserId\": 555," +
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

    static final String JSON_DECISION_WITH_NEW_FIELDS = "{" +
            "  \"user\": { \"key\": \"ad39231daeb043f2a9610414f08394b5\" }," +
            "  \"decisions\": {" +
            "    \"div1\": {" +
            "      \"adId\": 111," +
            "      \"creativeId\": 222," +
            "      \"height\": 250," +
            "      \"width\": 300," +
            "      \"ecpmPartition\": \"partition-a\"," +
            "      \"externalMetadata\": {" +
            "        \"campaignTag\": \"summer-sale\"," +
            "        \"priority\": 3" +
            "      }," +
            "      \"contents\": []," +
            "      \"events\": []," +
            "      \"adChain\": [" +
            "        {" +
            "          \"adId\": 999," +
            "          \"creativeId\": 888," +
            "          \"contents\": []," +
            "          \"events\": []" +
            "        }" +
            "      ]," +
            "      \"matchedPoints\": [" +
            "        { \"lat\": 35.995063, \"lon\": -78.908187 }," +
            "        { \"lat\": 40.689188, \"lon\": -74.044562 }" +
            "      ]," +
            "      \"pricing\": {" +
            "        \"price\": 3.0," +
            "        \"clearPrice\": 2.0," +
            "        \"modifiedPrice\": 1.5," +
            "        \"optimizedPrice\": 2.25," +
            "        \"eventMultiplier\": 1.1," +
            "        \"revenue\": 0.003," +
            "        \"rateType\": 2," +
            "        \"eCPM\": 3.0" +
            "      }" +
            "    }" +
            "  }" +
            "}";

    static final String JSON_MULTI_WINNERS = "{" +
            "  \"user\": {" +
            "    \"key\": \"ue1-a58d96713f6a41edb42695d24178e224\"" +
            "  }," +
            "  \"decisions\": {" +
            "    \"div1\": [" +
            "      {" +
            "        \"adId\": 20857910," +
            "        \"creativeId\": 18209784," +
            "        \"flightId\": 12637354," +
            "        \"campaignId\": 1772372," +
            "        \"priorityId\": 208307," +
            "        \"clickUrl\": \"https://e-9792.adzerk.net/r?e=eyJ2IjoiMS42IiwiYXYiOjkwNDA1MCwiYXQiOjE2MywiYnQiOjAsImNtIjoxNzcyMzcyLCJjaCI6NTE1MDksImNrIjp7fSwiY3IiOjE4MjA5Nzg0LCJkaSI6IjMwZTZhYjRmY2U4NzRkNzJiNWJjMjdhNjZhMDRhYWM2IiwiZGoiOjAsImlpIjoiZjMzY2Q3Y2RmMmY4NDk1MGFiZDM3ODg0NGY3Y2M3YzUiLCJkbSI6MywiZmMiOjIwODU3OTEwLCJmbCI6MTI2MzczNTQsImlwIjoiMTM2LjU2LjI5LjY0IiwibnciOjk3OTIsInBjIjowLCJlYyI6MCwiZXAiOm51bGwsInByIjoyMDgzMDcsInJ0IjowLCJycyI6NTAwLCJzYSI6IjgiLCJzYiI6ImktMDk4YzBmNmY4MDg5ZjY0OTUiLCJzcCI6Mjc1NzIsInN0IjoxMTMzODk4LCJ1ayI6InVlMS1hNThkOTY3MTNmNmE0MWVkYjQyNjk1ZDI0MTc4ZTIyNCIsInRzIjoxNjAzNTc5NjI4ODMzLCJwbiI6ImRpdjEiLCJnYyI6dHJ1ZSwiZ0MiOnRydWUsImdzIjoibm9uZSIsInR6IjoiVVRDIiwidXIiOiJodHRwczovL3d3dy5hZHplcmsuY29tIn0&s=b_aCdGnYWZtVF-nn761OKIlO4Ts\"," +
            "        \"impressionUrl\": \"https://e-9792.adzerk.net/i.gif?e=eyJ2IjoiMS42IiwiYXYiOjkwNDA1MCwiYXQiOjE2MywiYnQiOjAsImNtIjoxNzcyMzcyLCJjaCI6NTE1MDksImNrIjp7fSwiY3IiOjE4MjA5Nzg0LCJkaSI6IjMwZTZhYjRmY2U4NzRkNzJiNWJjMjdhNjZhMDRhYWM2IiwiZGoiOjAsImlpIjoiZjMzY2Q3Y2RmMmY4NDk1MGFiZDM3ODg0NGY3Y2M3YzUiLCJkbSI6MywiZmMiOjIwODU3OTEwLCJmbCI6MTI2MzczNTQsImlwIjoiMTM2LjU2LjI5LjY0IiwibnciOjk3OTIsInBjIjowLCJlYyI6MCwiZXAiOm51bGwsInByIjoyMDgzMDcsInJ0IjowLCJycyI6NTAwLCJzYSI6IjgiLCJzYiI6ImktMDk4YzBmNmY4MDg5ZjY0OTUiLCJzcCI6Mjc1NzIsInN0IjoxMTMzODk4LCJ1ayI6InVlMS1hNThkOTY3MTNmNmE0MWVkYjQyNjk1ZDI0MTc4ZTIyNCIsInRzIjoxNjAzNTc5NjI4ODM0LCJwbiI6ImRpdjEiLCJnYyI6dHJ1ZSwiZ0MiOnRydWUsImdzIjoibm9uZSIsInR6IjoiVVRDIiwiYmEiOjEsImZxIjowfQ&s=vPH8ubKqTKtyICkRmTpJeuySiiI\"," +
            "        \"contents\": [" +
            "          {" +
            "            \"type\": \"raw\"," +
            "            \"data\": {" +
            "              \"height\": 1," +
            "              \"width\": 1," +
            "              \"ctTitle\": \"German Sausages\"," +
            "              \"ctJoke\": \"I hate jokes about German sausages.  They're the wurst!\"" +
            "            }," +
            "            \"body\": \"{\\\"joke\\\": \\\"I hate jokes about German sausages.  They're the wurst!\\\",\\\"title\\\": \\\"German Sausages\\\",\\\"thumbnail\\\": \\\"\\\"}\"," +
            "            \"customTemplate\": \"{\\\"joke\\\": \\\"{{ctJoke}}\\\",\\\"title\\\": \\\"{{ctTitle}}\\\",\\\"thumbnail\\\": \\\"{{ctThumbnailUrl}}\\\"}\"" +
            "          }" +
            "        ]," +
            "        \"height\": 1," +
            "        \"width\": 1," +
            "        \"events\": []" +
            "      }," +
            "      {" +
            "        \"adId\": 20857908," +
            "        \"creativeId\": 18209782," +
            "        \"flightId\": 12637352," +
            "        \"campaignId\": 1772372," +
            "        \"priorityId\": 208307," +
            "        \"clickUrl\": \"https://e-9792.adzerk.net/r?e=eyJ2IjoiMS42IiwiYXYiOjkwNDA1MCwiYXQiOjE2MywiYnQiOjAsImNtIjoxNzcyMzcyLCJjaCI6NTE1MDksImNrIjp7fSwiY3IiOjE4MjA5NzgyLCJkaSI6IjMwZTZhYjRmY2U4NzRkNzJiNWJjMjdhNjZhMDRhYWM2IiwiZGoiOjEsImlpIjoiN2EwMDQwODUzZDdjNDg0ZDkxMzk4ZGFlM2E0ZGI4ZDgiLCJkbSI6MywiZmMiOjIwODU3OTA4LCJmbCI6MTI2MzczNTIsImlwIjoiMTM2LjU2LjI5LjY0IiwibnciOjk3OTIsInBjIjowLCJlYyI6MCwiZXAiOm51bGwsInByIjoyMDgzMDcsInJ0IjowLCJycyI6NTAwLCJzYSI6IjgiLCJzYiI6ImktMDk4YzBmNmY4MDg5ZjY0OTUiLCJzcCI6Mjc1NzIsInN0IjoxMTMzODk4LCJ1ayI6InVlMS1hNThkOTY3MTNmNmE0MWVkYjQyNjk1ZDI0MTc4ZTIyNCIsInRzIjoxNjAzNTc5NjI4ODM0LCJwbiI6ImRpdjEiLCJnYyI6dHJ1ZSwiZ0MiOnRydWUsImdzIjoibm9uZSIsInR6IjoiVVRDIiwidXIiOiJodHRwczovL3d3dy5hZHplcmsuY29tIn0&s=rsiZ3L8583Y-uBTQAvbZZMe0ABY\"," +
            "        \"impressionUrl\": \"https://e-9792.adzerk.net/i.gif?e=eyJ2IjoiMS42IiwiYXYiOjkwNDA1MCwiYXQiOjE2MywiYnQiOjAsImNtIjoxNzcyMzcyLCJjaCI6NTE1MDksImNrIjp7fSwiY3IiOjE4MjA5NzgyLCJkaSI6IjMwZTZhYjRmY2U4NzRkNzJiNWJjMjdhNjZhMDRhYWM2IiwiZGoiOjEsImlpIjoiN2EwMDQwODUzZDdjNDg0ZDkxMzk4ZGFlM2E0ZGI4ZDgiLCJkbSI6MywiZmMiOjIwODU3OTA4LCJmbCI6MTI2MzczNTIsImlwIjoiMTM2LjU2LjI5LjY0IiwibnciOjk3OTIsInBjIjowLCJlYyI6MCwiZXAiOm51bGwsInByIjoyMDgzMDcsInJ0IjowLCJycyI6NTAwLCJzYSI6IjgiLCJzYiI6ImktMDk4YzBmNmY4MDg5ZjY0OTUiLCJzcCI6Mjc1NzIsInN0IjoxMTMzODk4LCJ1ayI6InVlMS1hNThkOTY3MTNmNmE0MWVkYjQyNjk1ZDI0MTc4ZTIyNCIsInRzIjoxNjAzNTc5NjI4ODM1LCJwbiI6ImRpdjEiLCJnYyI6dHJ1ZSwiZ0MiOnRydWUsImdzIjoibm9uZSIsInR6IjoiVVRDIiwiYmEiOjEsImZxIjowfQ&s=LSewbXP5-7Xu1IkoqS5Dj0rxuqY\"," +
            "        \"contents\": [" +
            "          {" +
            "            \"type\": \"raw\"," +
            "            \"data\": {" +
            "              \"height\": 1," +
            "              \"width\": 1," +
            "              \"ctTitle\": \"Calendar Factory\"," +
            "              \"ctJoke\": \"I had a job in a calendar factory, but I got sacked for taking a couple of days off.\"" +
            "            }," +
            "            \"body\": \"{\\\"joke\\\": \\\"I had a job in a calendar factory, but I got sacked for taking a couple of days off.\\\",\\\"title\\\": \\\"Calendar Factory\\\",\\\"thumbnail\\\": \\\"\\\"}\"," +
            "            \"customTemplate\": \"{\\\"joke\\\": \\\"{{ctJoke}}\\\",\\\"title\\\": \\\"{{ctTitle}}\\\",\\\"thumbnail\\\": \\\"{{ctThumbnailUrl}}\\\"}\"" +
            "          }" +
            "        ]," +
            "        \"height\": 1," +
            "        \"width\": 1," +
            "        \"events\": []" +
            "      }," +
            "      {" +
            "        \"adId\": 20857909," +
            "        \"creativeId\": 18209783," +
            "        \"flightId\": 12637353," +
            "        \"campaignId\": 1772372," +
            "        \"priorityId\": 208307," +
            "        \"clickUrl\": \"https://e-9792.adzerk.net/r?e=eyJ2IjoiMS42IiwiYXYiOjkwNDA1MCwiYXQiOjE2MywiYnQiOjAsImNtIjoxNzcyMzcyLCJjaCI6NTE1MDksImNrIjp7fSwiY3IiOjE4MjA5NzgzLCJkaSI6IjMwZTZhYjRmY2U4NzRkNzJiNWJjMjdhNjZhMDRhYWM2IiwiZGoiOjIsImlpIjoiY2ViOWEwZDEzZDdjNDdlYWE4MzNkZjkxNDMxMDA1NGUiLCJkbSI6MywiZmMiOjIwODU3OTA5LCJmbCI6MTI2MzczNTMsImlwIjoiMTM2LjU2LjI5LjY0IiwibnciOjk3OTIsInBjIjowLCJlYyI6MCwiZXAiOm51bGwsInByIjoyMDgzMDcsInJ0IjowLCJycyI6NTAwLCJzYSI6IjgiLCJzYiI6ImktMDk4YzBmNmY4MDg5ZjY0OTUiLCJzcCI6Mjc1NzIsInN0IjoxMTMzODk4LCJ1ayI6InVlMS1hNThkOTY3MTNmNmE0MWVkYjQyNjk1ZDI0MTc4ZTIyNCIsInRzIjoxNjAzNTc5NjI4ODM1LCJwbiI6ImRpdjEiLCJnYyI6dHJ1ZSwiZ0MiOnRydWUsImdzIjoibm9uZSIsInR6IjoiVVRDIiwidXIiOiJodHRwczovL3d3dy5hZHplcmsuY29tIn0&s=dG-uPwpEoJysf9oEAiBpVWip5R4\"," +
            "        \"impressionUrl\": \"https://e-9792.adzerk.net/i.gif?e=eyJ2IjoiMS42IiwiYXYiOjkwNDA1MCwiYXQiOjE2MywiYnQiOjAsImNtIjoxNzcyMzcyLCJjaCI6NTE1MDksImNrIjp7fSwiY3IiOjE4MjA5NzgzLCJkaSI6IjMwZTZhYjRmY2U4NzRkNzJiNWJjMjdhNjZhMDRhYWM2IiwiZGoiOjIsImlpIjoiY2ViOWEwZDEzZDdjNDdlYWE4MzNkZjkxNDMxMDA1NGUiLCJkbSI6MywiZmMiOjIwODU3OTA5LCJmbCI6MTI2MzczNTMsImlwIjoiMTM2LjU2LjI5LjY0IiwibnciOjk3OTIsInBjIjowLCJlYyI6MCwiZXAiOm51bGwsInByIjoyMDgzMDcsInJ0IjowLCJycyI6NTAwLCJzYSI6IjgiLCJzYiI6ImktMDk4YzBmNmY4MDg5ZjY0OTUiLCJzcCI6Mjc1NzIsInN0IjoxMTMzODk4LCJ1ayI6InVlMS1hNThkOTY3MTNmNmE0MWVkYjQyNjk1ZDI0MTc4ZTIyNCIsInRzIjoxNjAzNTc5NjI4ODM1LCJwbiI6ImRpdjEiLCJnYyI6dHJ1ZSwiZ0MiOnRydWUsImdzIjoibm9uZSIsInR6IjoiVVRDIiwiYmEiOjEsImZxIjowfQ&s=9Cc2PNVr73d8p57vb_XjKmLpVfA\"," +
            "        \"contents\": [" +
            "          {" +
            "            \"type\": \"raw\"," +
            "            \"data\": {" +
            "              \"height\": 1," +
            "              \"width\": 1," +
            "              \"ctTitle\": \"Terrified of Lifts\"," +
            "              \"ctJoke\": \"I am terrified of lifts.  I'm going to start taking steps to avoid them.\"" +
            "            }," +
            "            \"body\": \"{\\\"joke\\\": \\\"I am terrified of lifts.  I'm going to start taking steps to avoid them.\\\",\\\"title\\\": \\\"Terrified of Lifts\\\",\\\"thumbnail\\\": \\\"\\\"}\"," +
            "            \"customTemplate\": \"{\\\"joke\\\": \\\"{{ctJoke}}\\\",\\\"title\\\": \\\"{{ctTitle}}\\\",\\\"thumbnail\\\": \\\"{{ctThumbnailUrl}}\\\"}\"" +
            "          }" +
            "        ]," +
            "        \"height\": 1," +
            "        \"width\": 1," +
            "        \"events\": []" +
            "      }" +
            "    ]" +
            "  }" +
            "}";

    static final String JSON_DECISION_WITH_MATCHEDPOINTS = "{" +
            "  \"user\": {" +
            "    \"key\": \"ue1-a58d96713f6a41edb42695d24178e224\"" +
            "  }," +
            "  \"decisions\": {" +
            "    \"div1\": [" +
            "      {" +
            "        \"adId\": 20857910," +
            "        \"creativeId\": 18209784," +
            "        \"flightId\": 12637354," +
            "        \"campaignId\": 1772372," +
            "        \"priorityId\": 208307," +
            "        \"clickUrl\": \"https://e-9792.adzerk.net/r?e=eyJ2IjoiMS42IiwiYXYiOjkwNDA1MCwiYXQiOjE2MywiYnQiOjAsImNtIjoxNzcyMzcyLCJjaCI6NTE1MDksImNrIjp7fSwiY3IiOjE4MjA5Nzg0LCJkaSI6IjMwZTZhYjRmY2U4NzRkNzJiNWJjMjdhNjZhMDRhYWM2IiwiZGoiOjAsImlpIjoiZjMzY2Q3Y2RmMmY4NDk1MGFiZDM3ODg0NGY3Y2M3YzUiLCJkbSI6MywiZmMiOjIwODU3OTEwLCJmbCI6MTI2MzczNTQsImlwIjoiMTM2LjU2LjI5LjY0IiwibnciOjk3OTIsInBjIjowLCJlYyI6MCwiZXAiOm51bGwsInByIjoyMDgzMDcsInJ0IjowLCJycyI6NTAwLCJzYSI6IjgiLCJzYiI6ImktMDk4YzBmNmY4MDg5ZjY0OTUiLCJzcCI6Mjc1NzIsInN0IjoxMTMzODk4LCJ1ayI6InVlMS1hNThkOTY3MTNmNmE0MWVkYjQyNjk1ZDI0MTc4ZTIyNCIsInRzIjoxNjAzNTc5NjI4ODMzLCJwbiI6ImRpdjEiLCJnYyI6dHJ1ZSwiZ0MiOnRydWUsImdzIjoibm9uZSIsInR6IjoiVVRDIiwidXIiOiJodHRwczovL3d3dy5hZHplcmsuY29tIn0&s=b_aCdGnYWZtVF-nn761OKIlO4Ts\"," +
            "        \"impressionUrl\": \"https://e-9792.adzerk.net/i.gif?e=eyJ2IjoiMS42IiwiYXYiOjkwNDA1MCwiYXQiOjE2MywiYnQiOjAsImNtIjoxNzcyMzcyLCJjaCI6NTE1MDksImNrIjp7fSwiY3IiOjE4MjA5Nzg0LCJkaSI6IjMwZTZhYjRmY2U4NzRkNzJiNWJjMjdhNjZhMDRhYWM2IiwiZGoiOjAsImlpIjoiZjMzY2Q3Y2RmMmY4NDk1MGFiZDM3ODg0NGY3Y2M3YzUiLCJkbSI6MywiZmMiOjIwODU3OTEwLCJmbCI6MTI2MzczNTQsImlwIjoiMTM2LjU2LjI5LjY0IiwibnciOjk3OTIsInBjIjowLCJlYyI6MCwiZXAiOm51bGwsInByIjoyMDgzMDcsInJ0IjowLCJycyI6NTAwLCJzYSI6IjgiLCJzYiI6ImktMDk4YzBmNmY4MDg5ZjY0OTUiLCJzcCI6Mjc1NzIsInN0IjoxMTMzODk4LCJ1ayI6InVlMS1hNThkOTY3MTNmNmE0MWVkYjQyNjk1ZDI0MTc4ZTIyNCIsInRzIjoxNjAzNTc5NjI4ODM0LCJwbiI6ImRpdjEiLCJnYyI6dHJ1ZSwiZ0MiOnRydWUsImdzIjoibm9uZSIsInR6IjoiVVRDIiwiYmEiOjEsImZxIjowfQ&s=vPH8ubKqTKtyICkRmTpJeuySiiI\"," +
            "        \"contents\": [" +
            "          {" +
            "            \"type\": \"raw\"," +
            "            \"data\": {" +
            "              \"height\": 1," +
            "              \"width\": 1," +
            "              \"ctTitle\": \"German Sausages\"," +
            "              \"ctJoke\": \"I hate jokes about German sausages.  They're the wurst!\"" +
            "            }," +
            "            \"body\": \"{\\\"joke\\\": \\\"I hate jokes about German sausages.  They're the wurst!\\\",\\\"title\\\": \\\"German Sausages\\\",\\\"thumbnail\\\": \\\"\\\"}\"," +
            "            \"customTemplate\": \"{\\\"joke\\\": \\\"{{ctJoke}}\\\",\\\"title\\\": \\\"{{ctTitle}}\\\",\\\"thumbnail\\\": \\\"{{ctThumbnailUrl}}\\\"}\"" +
            "          }" +
            "        ]," +
            "        \"height\": 1," +
            "        \"width\": 1," +
            "        \"events\": []," +
            "        \"matchedPoints\": [" +
            "          {" +
            "            \"lat\": \"35.995063\"," +
            "            \"lon\": \"-78.908187\"" +
            "          }," +
            "          {" +
            "            \"lat\": \"40.689188\"," +
            "            \"lon\": \"-74.044562\"" +
            "          }," +
            "          {" +
            "            \"lat\": \"29.979188\"," +
            "            \"lon\": \"31.134188\"" +
            "          }" +
            "      ]" +
            "      }" +
            "    ]" +
            "  }" +
            "}";
}
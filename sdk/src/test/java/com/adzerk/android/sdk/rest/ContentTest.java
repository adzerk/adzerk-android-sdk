package com.adzerk.android.sdk.rest;

import com.adzerk.android.sdk.AdzerkSdk;
import com.adzerk.android.sdk.AdzerkSdk.ResponseListener;
import com.adzerk.android.sdk.BuildConfig;
import com.adzerk.android.sdk.MockClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.adzerk.android.sdk.rest.Request.Builder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;
import java.util.concurrent.CountDownLatch;

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
        sdk = AdzerkSdk.getInstance(mockClient);
    }

    @Test
    public void itShouldDeserializeResponseContent() {
        final CountDownLatch latch = new CountDownLatch(1);

        sdk.request(createTestRequest(), new ResponseListener() {
            @Override
            public void success(Response response) {
                // Content
                Decision div1 = response.getDecision("div1");
                List<Content> contents = div1.getContents();
                assertThat(contents.size()).isEqualTo(1);
                Content div1Content = contents.get(0);
                assertThat(div1Content.getType()).isEqualTo(Content.TYPE_HTML);
                assertThat(div1Content.getTemplate()).isEqualTo(Content.TEMPLATE_IMAGE);
                assertThat(div1Content.getCreativeData()).containsKeys("imageUrl", "title");
                assertThat(div1Content.getBody()).isNotEmpty();

                // customData:
                assertThat(div1Content.getCreativeMetadata())
                        .isNotEmpty()
                        .containsOnlyKeys("foo", "bar");

                assertThat(div1Content.getCreativeMetadata("foo")).isEqualTo(new Double(42));
                assertThat(div1Content.getCreativeMetadata("bar")).isEqualTo("some string");

                // verify customData JsonObject & raw JSON String:
                Gson gson = new GsonBuilder().create();
                JsonObject expectedJsonObject = gson.fromJson(customData, JsonObject.class);
                assertThat(div1Content.getCreativeMetadataAsJson()).isNotNull();
                assertThat(div1Content.getCreativeMetadataAsJson()).isEqualToComparingFieldByField(expectedJsonObject);
                assertThat(div1Content.getCreativeMetadataAsString().replaceAll("\\s+", ""))
                      .isEqualToIgnoringCase(customData.replaceAll("\\s+", ""));

                latch.countDown();
            }

            @Override
            public void error(RetrofitError error) {
                fail(error.getMessage());
            }
        });

    }

    private Request createTestRequest() {
        return new Builder()
                .addPlacement(new Placement("div1", 9709L, 70464L, 5))
                .build();
    }

    private static String customData = "{ \"foo\": 42, \"bar\": \"some string\" }";
    
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
              "          \"height\": 250," +
              "          \"customData\": { \"foo\": 42, \"bar\": \"some string\" }" +
              "        }," +
              "      \"body\": \"<a href='...'><img src='http://static.adzerk.net/cat-eating-spaghetti.jpg' title='ZOMG LOOK AT THIS FRICKING CAT' width=350 height=350></a>\"" +
              "      }" +
              "    ]," +
              "    \"impressionUrl\": \"http://engine.adzerk.net/i.gif?...\"" +
              "    }" +
              "  }" +
              "}";
    }

}
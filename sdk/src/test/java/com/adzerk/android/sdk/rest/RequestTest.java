package com.adzerk.android.sdk.rest;

import com.adzerk.android.sdk.AdzerkSdk;
import com.adzerk.android.sdk.BuildConfig;
import com.adzerk.android.sdk.TestData;
import com.adzerk.android.sdk.gson.FlattenTypeAdapterFactory;
import com.adzerk.android.sdk.rest.Request.Builder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(sdk=25, constants=BuildConfig.class)
public class RequestTest {

    AdzerkSdk sdk;

    final static Placement placement = new Placement("div1", 9709, 70464, 5);
    final static List<Placement> placements = Arrays.asList(placement);

    @Mock
    AdzerkService api;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        sdk = AdzerkSdk.createInstance(api);
    }

    @Test
    public void itShouldThrowOnEmptyPlacements() {
        try {
            new Builder(new ArrayList<Placement>()).build();
            fail("Failed to throw on empty placements");
        } catch(IllegalArgumentException e) {
            // success
        }
        
        try {
            new Builder().build();
            fail("Failed to throw on empty placements");
        } catch(IllegalStateException e) {
            // success
        }
    }

    @Test
    public void itShouldAddPlacement() {
        try {
            Placement div2 = new Placement("div2", 9709, 70464, 5);

            Request request = new Builder()
                  .addPlacement(div2)
                  .build();

            assertThat(request.getPlacements().contains(div2));
        } catch(IllegalArgumentException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void itShouldBuildUser() {

        try {
            String key = "testUserKey";

            Request request = new Builder(placements)
                  .setUser(new User(key))
                  .build();

            assertThat(request.getUser().getKey()).isEqualTo(key);
        } catch (IllegalArgumentException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void itShouldSetUserConsent() {

        try {
            String key = "testUserKey";

            Request request = new Builder(placements)
                  .setUser(new User(key))
                  .setConsent(new Consent(true))
                  .build();

            assertThat(request.getUser().getKey()).isEqualTo(key);
            assertThat(request.getConsent().isGdpr()).isEqualTo(true);
        } catch (IllegalArgumentException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void itShouldBuildKeywords() {

        try {
            Set<String> keywords = new HashSet<>();
            keywords.add("key1");
            keywords.add("key2");
            keywords.add("key3");

            Request request = new Builder(placements)
                  .setKeywords(keywords)
                  .build();

            assertThat(request.getKeywords().containsAll(Arrays.asList("key1", "key2", "key3")));
        } catch (IllegalArgumentException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void itShouldAddKeywords() {
        try {
            Set<String> keywords = new HashSet<>();
            keywords.add("key1");
            keywords.add("key2");
            keywords.add("key3");

            Request request = new Builder(placements)
                  .setKeywords(keywords)
                  .addKeywords("key4")
                  .addKeywords("key5")
                  .build();

            assertThat(request.getKeywords().containsAll(Arrays.asList("key1", "key2", "key3", "key4", "key5")));

        } catch (IllegalArgumentException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void itShouldEnforceUniqueKeywords() {
        try {
            Request request = new Builder(placements)
                  .addKeywords("duplicateKey")
                  .addKeywords("duplicateKey")
                  .build();

            assertThat(request.getKeywords().size()).isEqualTo(1);

        } catch (IllegalArgumentException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void itShouldBuildReferrer() {

        try {
            String referrer = "http://referrer.com";

            Request request = new Builder(placements)
                  .setReferrer(referrer)
                  .build();

            assertThat(request.getReferrer()).isEqualTo(referrer);

        } catch (IllegalArgumentException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void itShouldBuildUrl() {
        try {
            String url = "http://test.com";

            Request request = new Builder(placements)
                  .setUrl(url)
                  .build();

            assertThat(request.getUrl()).isEqualTo(url);

        } catch (IllegalArgumentException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void itShouldBuildIp() {
        try {
            String ip = "192.168.1.1";

            Request request = new Builder(placements)
                  .setIp(ip)
                  .build();

            assertThat(request.getIp()).isEqualTo(ip);

        } catch (IllegalArgumentException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void itShouldBuildBlockedCreatives() {

        try {
            Set<Integer> blocked = new HashSet(Arrays.asList(1, 2, 3, 4, 5));

            Request request = new Builder(placements)
                  .setBlockedCreatives(blocked)
                  .build();

            assertThat(request.getBlockedCreatives()).containsAll(blocked);

        } catch (IllegalArgumentException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void itShouldAddBlockedCreatives() {
        try {
            int blocked1 = 1;
            int blocked2 = 2;

            Request request = new Builder(placements)
                  .addBlockedCreatives(blocked1, blocked2)
                  .build();

            assertThat(request.getBlockedCreatives()).contains(blocked1);
            assertThat(request.getBlockedCreatives()).contains(blocked2);
        } catch (IllegalArgumentException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void itShouldBuildFlightViewTimes() {
        try {
            long time1 = 1401580800L;
            long time2 = 1404172800L;
            long time3 = 1406851200L;
            long time4 = 1409529600L;
            long time5 = 1412121600L;

            // times for flight id = 2
            long flightViewTimes2[] = {time4, time5};

            // Adding flight view times
            Request request = new Builder(placements)
                  .setFlightViewTimes(1, time1, time2, time3)
                  .setFlightViewTimes(2, flightViewTimes2)
                  .build();

            assertThat(request.getAllFlightViewTimes()).containsKeys(1, 2);
            assertThat(request.getFlightViewTimes(1)).containsAll(Arrays.<Long>asList(time1, time2, time3));
            assertThat(request.getFlightViewTimes(2)).containsAll(Arrays.<Long>asList(time4, time5));

        } catch (IllegalArgumentException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void itShouldReturnEmptyFlightViewList() {
        try {
            Request request = new Builder(placements).build();

            // query flight times for invalid flight id
            List<Long> times = request.getFlightViewTimes(2);

            // verify empty list is returned
            assertThat(times).isNotNull();
            assertThat(times).isEmpty();

        }  catch (IllegalArgumentException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void itShouldDisableBotFiltering() {

        try {
            Request request = new Builder(placements).build();
            assertThat(request.isBotFilteringEnabled()).isEqualTo(false);
        } catch (IllegalArgumentException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void itShouldEnableBotFiltering() {

        try {
            Request request = new Builder(placements).setBotFilteringEnabled(true).build();
            assertThat(request.isBotFilteringEnabled()).isEqualTo(true);
        } catch (IllegalArgumentException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void itShouldBuildAdditionalOptions() {
        String[] strings = new String[] {"value1","value2","value3" };
        try {
            Request request = new Builder(placements)
                    .addAdditionalOption("string", "string1")
                    .addAdditionalOption("int", 123)
                    .addAdditionalOption("boolean", true)
                    .addAdditionalOption("strings", strings)
                    .addAdditionalOption("javaObject", new TestData.ObjectWithDefaultFieldNames("foo", 99, false))
                    .addAdditionalOption("customObject", new TestData.ObjectWithCustomFieldNames("foo", 99, false))
                    .build();

            JsonElement stringElement = request.additionalOptions.get("string");
            assertNotNull(stringElement);
            assertThat(stringElement instanceof JsonPrimitive);
            assertEquals("string1", stringElement.getAsString());

            JsonElement intElement = request.additionalOptions.get("int");
            assertNotNull(intElement);
            assertThat(intElement instanceof JsonPrimitive);
            assertEquals(123, intElement.getAsInt());

            JsonElement booleanElement = request.additionalOptions.get("boolean");
            assertNotNull(booleanElement);
            assertThat(booleanElement instanceof JsonPrimitive);
            assertEquals(true, booleanElement.getAsBoolean());

            JsonElement stringsElement = request.additionalOptions.get("strings");
            assertNotNull(stringsElement);
            assertThat(stringsElement instanceof JsonArray);
            JsonArray stringsArray = stringsElement.getAsJsonArray();
            assertEquals(strings[0], stringsArray.get(0).getAsString());
            assertEquals(strings[1], stringsArray.get(1).getAsString());
            assertEquals(strings[2], stringsArray.get(2).getAsString());

            JsonElement javaObjectElement = request.additionalOptions.get("javaObject");
            assertNotNull(javaObjectElement);
            assertThat(javaObjectElement.isJsonObject());
            JsonObject myObjectWithDefaultFieldNames = javaObjectElement.getAsJsonObject();
            assertNotNull(myObjectWithDefaultFieldNames.get("stringField"));
            assertEquals("foo", myObjectWithDefaultFieldNames.get("stringField").getAsString());
            assertNotNull(myObjectWithDefaultFieldNames.get("intField"));
            assertEquals(99, myObjectWithDefaultFieldNames.get("intField").getAsInt());
            assertNotNull(myObjectWithDefaultFieldNames.get("booleanField"));
            assertEquals(false, myObjectWithDefaultFieldNames.get("booleanField").getAsBoolean());

            JsonElement customObjectElement = request.additionalOptions.get("customObject");
            assertNotNull(customObjectElement);
            assertThat(customObjectElement.isJsonObject());
            JsonObject myObjectWithCustomFieldNames = customObjectElement.getAsJsonObject();
            assertNotNull(myObjectWithCustomFieldNames.get("name"));
            assertEquals("foo", myObjectWithCustomFieldNames.get("name").getAsString());
            assertNotNull(myObjectWithCustomFieldNames.get("age"));
            assertEquals(99, myObjectWithCustomFieldNames.get("age").getAsInt());
            assertNotNull(myObjectWithCustomFieldNames.get("employee"));
            assertEquals(false, myObjectWithCustomFieldNames.get("employee").getAsBoolean());

            Gson gson = new GsonBuilder().registerTypeAdapterFactory(new FlattenTypeAdapterFactory()).create();
            String json = gson.toJson(request);
            assertNotNull(json);
            assertEquals(jsonRequest, json);
            System.out.println(json);

        }  catch (IllegalArgumentException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    String jsonRequest = "{\"placements\":[{" +
            "\"divName\":\"div1\"," +
            "\"networkId\":0," +
            "\"siteId\":9709," +
            "\"adTypes\":[70464,5]}]," +
            "\"enableBotFiltering\":false," +
            "\"javaObject\":{" +
            "\"stringField\":\"foo\"," +
            "\"intField\":99," +
            "\"booleanField\":false" +
            "}," +
            "\"boolean\":true," +
            "\"string\":\"string1\"," +
            "\"strings\":[\"value1\",\"value2\",\"value3\"]," +
            "\"customObject\":{" +
            "\"name\":\"foo\"," +
            "\"age\":99," +
            "\"employee\":false" +
            "}," +
            "\"int\":123" +
            "}";
}
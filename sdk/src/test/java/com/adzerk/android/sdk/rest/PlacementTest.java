package com.adzerk.android.sdk.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;


public class PlacementTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void itShouldThrowOnNoAdType() {
        try {
            Placement div1 = new Placement("div1", 9999, 77777);
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void itShouldThrowOnNullDivName() {
        try {
            Placement div1 = new Placement(null, 9999, 77777, 5);
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void itShouldCreatePlacement() {
        try {
            final String divName = "div1";
            final long networkId = 9999;
            final long siteId = 77777;
            final int adType5 = 5;
            final int adType6 = 6;

            Placement div1 = new Placement(divName, networkId, siteId, adType5, adType6);

            assertThat(div1.getDivName()).isEqualTo(divName);
            assertThat(div1.getNetworkId()).isEqualTo(networkId);
            assertThat(div1.getSiteId()).isEqualTo(siteId);
            assertThat(div1.getAdTypes()).contains(adType5);
            assertThat(div1.getAdTypes()).contains(adType6);

        } catch(IllegalArgumentException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void itShouldSetOptionalAttributes() {
        try {
            final Set<Integer> zoneIds = new HashSet(Arrays.asList(10, 11, 12));
            final int campaignId = 100;
            final int flightId = 3;
            final int adId = 42;
            final String clickUrl = "clickUrl";
            final Set<Integer> eventIds = new HashSet(Arrays.asList(1, 2, 3));

            // create placement
            Placement div1 = new Placement("div1", 9999, 77777, 5, 6);

            // set options attributes
            div1.setZoneIds(zoneIds);
            div1.setCampaignId(campaignId);
            div1.setFlightId(flightId);
            div1.setAdId(adId);
            div1.setClickUrl(clickUrl);
            div1.setEventIds(eventIds);

            // verify
            assertThat(div1.getZoneIds()).containsAll(zoneIds);
            assertThat(div1.getCampaignId()).isEqualTo(campaignId);
            assertThat(div1.getFlightId()).isEqualTo(flightId);
            assertThat(div1.getAdId()).isEqualTo(adId);
            assertThat(div1.getClickUrl()).isEqualTo(clickUrl);
            assertThat(div1.getEventIds()).containsAll(eventIds);

        } catch(IllegalArgumentException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void itShouldAddOptionalAttributes() {
        try {
            Placement div1 = new Placement("div1", 9999, 77777, 5, 6);

            // add zone and event ids
            div1.addZoneIds(10, 11, 11, 12);
            div1.addEventIds(1, 1, 2);

            // verify (no duplicate ids)
            assertThat(div1.getZoneIds()).containsAll(new HashSet(Arrays.asList(10, 11, 12)));
            assertThat(div1.getEventIds()).containsAll(new HashSet(Arrays.asList(1, 2)));
            assertThat(div1.getZoneIds().size()).isEqualTo(3);
            assertThat(div1.getEventIds().size()).isEqualTo(2);

        } catch(IllegalArgumentException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void itShouldSetProperties() {
        Placement div1 = new Placement("div1", 123, 456, 4, 5);

        // add some properties
        div1.addProperty("foo", 42);
        div1.addProperty("bar", "example");
        div1.addProperty("baz", Arrays.asList("one", "two"));

        assertThat(div1.getProperties()).containsKeys("foo", "bar", "baz");
        assertThat(div1.getProperties().get("foo")).isEqualTo(42);
        assertThat(div1.getProperties().get("bar")).isEqualTo("example");
        assertThat(div1.getProperties().get("baz")).isEqualTo(Arrays.asList("one", "two"));
    }

    @Test
    public void itShouldSerializePlacements() {

        Gson gson = new GsonBuilder().create();

        Placement div1 = new Placement("div1", 123, 456, 4, 5);
        div1.addZoneIds(789);
        div1.setCampaignId(123);
        div1.setFlightId(456);
        div1.setAdId(789);
        div1.addEventIds(12, 13, 14);
        div1.setClickUrl("http://adzerk.com/");
        div1.addProperty("foo", 42);
        div1.addProperty("bar", "example");
        div1.addProperty("baz", Arrays.asList("one", "two"));

        // serialize Placement to json
        String json = gson.toJson(div1);
        JsonElement jsonElement = gson.toJsonTree(div1);

        // expected json
        JsonElement expectedJsonElement = gson.fromJson(jsonPlacement1, JsonElement.class);

        // verify results
        assertThat(json).isNotEmpty();
        assertThat(jsonElement).isEqualToComparingFieldByField(expectedJsonElement);
    }


    public static final String jsonPlacement1 = "{" +
          "  \"divName\": \"div1\"," +
          "  \"networkId\": 123," +
          "  \"siteId\": 456," +
          "  \"adTypes\": [4, 5]," +
          "  \"zoneIds\": [789]," +
          "  \"campaignId\": 123," +
          "  \"flightId\": 456," +
          "  \"adId\": 789," +
          "  \"clickUrl\": \"http://adzerk.com/\"," +
          "  \"eventIds\": [12,13,14]," +
          "  \"properties\": {" +
          "    \"foo\": 42,\n" +
          "    \"bar\": \"example\"," +
          "    \"baz\": [\"one\", \"two\"]" +
          "  }" +
          "}";

}
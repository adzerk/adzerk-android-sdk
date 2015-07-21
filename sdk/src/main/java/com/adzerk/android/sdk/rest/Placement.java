package com.adzerk.android.sdk.rest;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Every Request must contain one or more Placements. Each placement represents a "slot" in which an ad may be served.
 *
 *  {
 *      "placements": [
 *      {
 *          "divName": "div1",
 *          "networkId": 123,
 *          "siteId": 456,
 *          "adTypes": [5],
 *          "eventIds": [12,13,14],
 *          "properties": {
 *          "foo": 42,
 *           "bar": "example",
 *          "baz": ["one", "two"]
 *      }, ...
 */
public class Placement {
    // unique name for the placement (required)
    private String divName;

    // network id to use when selecting an ad (required)
    private long networkId;

    // site id to use when selecting an ad (required)
    private long siteId;

    // one or more integer ad types to use when selecting an ad (required)
    private Set<Integer> adTypes;

    // zero or more zone ids to use when selecting an ad
    private Set<Integer> zoneIds;

    // campaign id; if specified, only consider ads in that campaign
    private Integer campaignId;

    // flight id; if specified, only consider ads in that flight
    private Integer flightId;

    // ad (flight-creative map) id; if specified, only serve that ad if possible
    private Integer adId;

    // URL that should be used as the click-through target for the ad
    private String clickUrl;

    // hash of key/value pairs used for custom targeting
    private Map<String, Object> properties;

    // array of numeric event types. Requests tracking URLs for custom events
    private Set<Integer> eventIds;


    /**
     * Creates a Placement. You may request multiple ads using a single Request by specifying multiple placements.
     * A Placement identifies a place where an ad can be served. It has a unique divName.
     *
     * The Response returns a corresponding Decision for each Placement. A Decision provides the ad that was
     * selected to be served for a given Placement. The Response relates a Decision to Placement by divName.
     *
     * @param divName       unique name for the placement
     * @param networkId     network id to use when selecting an ad
     * @param siteId        site id to use when selecting an ad
     * @param adTypes       one or more integer ad types to use when selecting an ad
     */
    public Placement(@NonNull String divName, long networkId, long siteId, int... adTypes) {
        setDivName(divName);
        setNetworkId(networkId);
        setSiteId(siteId);

        if (adTypes == null || adTypes.length < 1) {
            throw new IllegalArgumentException("At least one ad type must be specified");
        }

        for (int adType : adTypes ) {
            addAdType(adType);
        }
    }

    /**
     *
     * @return
     */
    public String getDivName() {
        return divName;
    }

    /**
     *
     * @param divName
     */
    public void setDivName(String divName) {
        this.divName = divName;
    }

    /**
     *
     * @return
     */
    public long getNetworkId() {
        return networkId;
    }

    /**
     *
     * @param networkId
     */
    public void setNetworkId(long networkId) {
        this.networkId = networkId;
    }

    /**
     *
     * @return
     */
    public long getSiteId() {
        return siteId;
    }

    /**
     *
     * @param siteId
     */
    public void setSiteId(long siteId) {
        this.siteId = siteId;
    }

    /**
     *
     * @return
     */
    public Set<Integer> getAdTypes() {
        return adTypes;
    }

    /**
     *
     * @param adType
     */
    private void addAdType(int adType) {
        if (adTypes == null) {
            adTypes = new HashSet<>();
        }
        adTypes.add(adType);
    }

    /**
     *
     * @return
     */
    public Set<Integer> getZoneIds() {
        return zoneIds;
    }

    /**
     *
     * @param zoneIds
     */
    public void setZoneIds(@Nullable Set<Integer> zoneIds) {
        this.zoneIds = zoneIds;
    }

    /**
     *
     * @param zoneIds
     */
    public void addZoneIds(int... zoneIds) {
        if (this.zoneIds == null) {
            this.zoneIds = new HashSet<>();
        }
        for (int zoneId : zoneIds) {
            this.zoneIds.add(zoneId);
        }
    }

    /**
     *
     * @return
     */
    public int getCampaignId() {
        return campaignId;
    }

    /**
     *
     * @param campaignId
     */
    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    /**
     *
     * @return
     */
    public int getFlightId() {
        return flightId;
    }

    /**
     *
     * @param flightId
     */
    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    /**
     *
     * @return
     */
    public int getAdId() {
        return adId;
    }

    /**
     *
     * @param adId
     */
    public void setAdId(int adId) {
        this.adId = adId;
    }

    /**
     *
     * @return
     */
    public String getClickUrl() {
        return clickUrl;
    }

    /**
     *
     * @param clickUrl
     */
    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }

    /**
     *
     * @return
     */
    public Map<String, Object> getProperties() {
        return properties;
    }

    /**
     * A map of key/value pairs used for custom targeting. Values
     *
     * “properties”: {
     *    “numeral-key-name”: 42,
     *    "string-key-name": "the answer to the ultimate question",
     *    "array-key-name": ["life", "the universe", "everything"]
     * }
     *
     * @param properties
     */
    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    /**
     * Add key/value pair used for custom targeting.
     *
     * @param key
     * @param value
     */
    public void addProperty(String key, Object value) {
        if (properties == null) {
            properties = new HashMap<>();
        }
        properties.put(key, value);
    }

    /**
     *
     * @return
     */
    public Set<Integer> getEventIds() {
        return eventIds;
    }

    /**
     *
     * @param eventIds
     */
    public void setEventIds(Set<Integer> eventIds) {
        this.eventIds = eventIds;
    }

    /**
     *
     * @param eventIds
     */
    public void addEventIds(int... eventIds) {
        if (this.eventIds == null) {
            this.eventIds = new HashSet<>();
        }
        for (int eventId : eventIds) {
            this.eventIds.add(eventId);
        }
    }
}

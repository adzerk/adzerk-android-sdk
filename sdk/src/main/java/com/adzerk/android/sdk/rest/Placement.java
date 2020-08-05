package com.adzerk.android.sdk.rest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * To make an ad {@link Request}, you will specify one of more Placements. Each Placement has a unique name to
 * identify a place where an can be served in your app. To request multiple ads in a single {@link Request} you
 * specify multiple Placements.
 * <p>
 * <pre>
 * {@code
 * // Build a Request with multiple Placements:
 * Request request = new Request.Builder()
 *     .addPlacement(new Placement(name1, networkId, siteId, adTypes))
 *     .addPlacement(new Placement(name2, networkId, siteId, adTypes))
 *     .build();
 * }
 * </pre>
 *
 *
 */
public class Placement {

    // unique name for the placement (required)
    String divName;

    // network id to use when selecting an ad (required)
    long networkId;

    // site id to use when selecting an ad (required)
    long siteId;

    // one or more integer ad types to use when selecting an ad (required)
    Set<Integer> adTypes;

    // zero or more zone ids to use when selecting an ad
    Set<Integer> zoneIds;

    // campaign id; if specified, only consider ads in that campaign
    Integer campaignId;

    // flight id; if specified, only consider ads in that flight
    Integer flightId;

    // ad (flight-creative map) id; if specified, only serve that ad if possible
    Integer adId;

    // URL that should be used as the click-through target for the ad
    String clickUrl;

    // hash of key/value pairs used for custom targeting
    Map<String, Object> properties;

    // array of numeric event types. Requests tracking URLs for custom events
    Set<Integer> eventIds;


    /**
     * Creates a Placement with all required fields. A Placement identifies a place where an ad can be served
     * and has a unique divName. To request multiple ads using a single Request you specify multiple Placements.
     * The SDK will provide the default networkId.
     *
     * <pre>
     * {@code
     * // create placement with required arguments
     * Placement div1 = new Placement("div1", 1L, 5);
     * }
     * </pre>
     *
     * The Response to an ad Request returns a corresponding Decision for each Placement. The Decision contains the
     * ad that was selected for a given Placement. The DecisionResponse relates a Decision to Placement by divName.
     *
     * @param divName       unique name for the placement
     * @param siteId        site id to use when selecting an ad
     * @param adTypes       one or more integer ad types to use when selecting an ad
     */
    public Placement(@NonNull String divName, long siteId, int... adTypes) {
        setDivName(divName);
        setSiteId(siteId);

        if (adTypes == null || adTypes.length < 1) {
            throw new IllegalArgumentException("At least one ad type must be specified");
        }

        addAdTypes(adTypes);
    }

    /**
     * Creates a Placement and for specified networkId. A Placement identifies a place where an ad can be served
     * and has a unique divName. To request multiple ads using a single Request you specify multiple Placements.
     *
     * <pre>
     * {@code
     * // create placement for networkId with required arguments
     * Placement div1 = new Placement("div1", 1L, 2L, 5);
     * }
     * </pre>
     *
     * The Response to an ad Request returns a corresponding Decision for each Placement. The Decision contains the
     * ad that was selected for a given Placement. The DecisionResponse relates a Decision to Placement by divName.
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

        addAdTypes(adTypes);
    }

    /**
     * Returns the unique name for the placement
     *
     * @return  name to identify this placement in a Request
     */
    public String getDivName() {
        return divName;
    }

    /**
     * Set the name for the placement. A Request can have multiple Placements and each one should have a unique name.
     *
     * @param divName   name to identify this placement in a Request
     */
    public void setDivName(String divName) {
        this.divName = divName;
    }

    /**
     * Returns the numeric network id to use when selecting ads
     *
     * @return  numeric id of network
     */
    public long getNetworkId() {
        return networkId;
    }

    /**
     * Sets the numeric network id to use when selecting ads
     *
     * @param networkId numeric id of network
     */
    public void setNetworkId(long networkId) {
        this.networkId = networkId;
    }

    /**
     * Returns the numeric site id to use when selecting an ad
     *
     * @return  numeric id of site
     */
    public long getSiteId() {
        return siteId;
    }

    /**
     * Sets the numeric site id to use when selecting an ad
     *
     * @param siteId    numeric id of site
     */
    public void setSiteId(long siteId) {
        this.siteId = siteId;
    }

    /**
     * Returns set of integer ad types use use when selecting an ad
     *
     * @return  integers representing ad types
     */
    public Set<Integer> getAdTypes() {
        return adTypes;
    }

    /**
     * Adds an ad type to the set used use when selecting an ad
     *
     * @param adTypes    one or more ad types
     */
    private void addAdTypes(int... adTypes) {
        if (this.adTypes == null) {
            this.adTypes = new HashSet<>();
        }
        for (int adType : adTypes ) {
            this.adTypes.add(adType);
        }
    }

    /**
     * Returns set of integers representing the zone ids to use when selecting an ad
     *
     * @return  zone ids
     */
    public Set<Integer> getZoneIds() {
        return zoneIds;
    }

    /**
     * Set zone ids to use when selecting an ad
     * @param zoneIds   set of zone ids
     * @return
     */
    public Placement setZoneIds(@Nullable Set<Integer> zoneIds) {
        this.zoneIds = zoneIds;
        return this;
    }

    /**
     * Add zone ids to use when selecting an ad
     * @param zoneIds   one or more zone ids
     * @return
     */
    public Placement addZoneIds(int... zoneIds) {
        if (this.zoneIds == null) {
            this.zoneIds = new HashSet<>();
        }
        for (int zoneId : zoneIds) {
            this.zoneIds.add(zoneId);
        }
        return this;
    }

    /**
     * Returns the campaign id used for selections ads
     *
     * @return numeric campaign id
     */
    public int getCampaignId() {
        return campaignId;
    }

    /**
     * Sets the numeric campaign id; if specified, only consider ads in that campaign
     *
     * @param campaignId numeric campaign id
     * @return the placement
     */
    public Placement setCampaignId(int campaignId) {
        this.campaignId = campaignId;
        return this;
    }

    /**
     * Returns the numeric flight id; if specified, only consider ads in that flight
     *
     * @return  numeric flight id
     */
    public int getFlightId() {
        return flightId;
    }

    /**
     * Set the numeric flight id; if specified, only consider ads in that flight
     *
     * @param flightId numeric flight id
     * @return the placement
     */
    public Placement setFlightId(int flightId) {
        this.flightId = flightId;
        return this;
    }

    /**
     * Returns the numeric ad id; if specified, only serve that ad if possible
     *
     * @return numeric ad id
     */
    public int getAdId() {
        return adId;
    }

    /**
     * Sets the numeric ad id; if specified, only serve that ad if possible
     *
     * @param adId numeric ad id
     * @return the placement
     */
    public Placement setAdId(int adId) {
        this.adId = adId;
        return this;
    }

    /**
     * Returns the URL that should be used as the click-through target for the ad
     *
     * @return  url used as click-through
     */
    public String getClickUrl() {
        return clickUrl;
    }

    /**
     * Set the URL that should be used as the click-through target for the ad
     *
     * @param clickUrl  url to use for ad click-through
     * @return the placement
     */
    public Placement setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
        return this;
    }

    /**
     * Returns the map of key/value pairs used for custom targeting
     *
     * @return  properties specified for custom targeting
     */
    public Map<String, Object> getProperties() {
        return properties;
    }

    /**
     * Sets the map of key/value pairs used for custom targeting
     *
     * @param properties key-value pairs
     * @return the placement
     */
    public Placement setProperties(Map<String, Object> properties) {
        this.properties = properties;
        return this;
    }

    /**
     * Add key/value pair used for custom targeting.
     *
     * @param key   property key
     * @param value property value
     * @return the placement
     */
    public Placement addProperty(String key, Object value) {
        if (properties == null) {
            properties = new HashMap<>();
        }
        properties.put(key, value);
        return this;
    }

    /**
     * Returns set of numeric event types used to request tracking URLs for custom events
     *
     * @see Event
     * @return  event types for requesting tracking URLs
     */
    public Set<Integer> getEventIds() {
        return eventIds;
    }

    /**
     * Set the numeric event type used to request tracking URLs for custom events.
     * See the {@link Event} class for the list of defined Event ids.
     *
     * @param eventIds  numeric event types
     * @return the placement
     */
    public Placement setEventIds(Set<Integer> eventIds) {
        this.eventIds = eventIds;
        return this;
    }

    /**
     * Add one or more event types to the set used to request tracking URLs for custom events.
     * See the {@link Event} class for the list of defined Event ids.
     *
     * @param eventIds  one or more event type ids
     * @return the placement
     */
    public Placement addEventIds(int... eventIds) {
        if (this.eventIds == null) {
            this.eventIds = new HashSet<>();
        }
        for (int eventId : eventIds) {
            this.eventIds.add(eventId);
        }
        return this;
    }
}

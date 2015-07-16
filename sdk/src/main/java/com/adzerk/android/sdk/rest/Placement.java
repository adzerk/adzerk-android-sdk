package com.adzerk.android.sdk.rest;

import android.util.Property;

import java.net.URL;
import java.util.List;

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
    private Long networkId;

    // site id to use when selecting an ad (required)
    private Long siteId;

    // one or more integer ad types to use when selecting an ad (required)
    private List<Integer> adTypes;

    // zero or more zone ids to use when selecting an ad
    private List<Integer> zoneIds;

    // campaign id; if specified, only consider ads in that campaign
    private Integer campaignId;

    // flight id; if specified, only consider ads in that flight
    private Integer flightId;

    // ad (flight-creative map) id; if specified, only serve that ad if possible
    private Integer adId;

    // URL that should be used as the click-through target for the ad
    private URL clickUrl;

    // hash of key/value pairs used for custom targeting
    private List<Property> properties;

    // array of numeric event types. Requests tracking URLs for custom events
    private List<EventId> eventIds;


    public Placement(String divName, long networkId, long siteId, List<Integer> adTypes) {
        setDivName(divName);
        setNetworkId(networkId);
        setSiteId(siteId);
        setAdTypes(adTypes);
    }

    public String getDivName() {
        return divName;
    }

    public void setDivName(String divName) {
        this.divName = divName;
    }

    public Long getNetworkId() {
        return networkId;
    }

    public void setNetworkId(Long networkId) {
        this.networkId = networkId;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public List<Integer> getAdTypes() {
        return adTypes;
    }

    public void setAdTypes(List<Integer> adTypes) {
        this.adTypes = adTypes;
    }

    public void addAdType(Integer adType) {
        adTypes.add(adType);
    }

    public List<Integer> getZoneIds() {
        return zoneIds;
    }

    public void setZoneIds(List<Integer> zoneIds) {
        this.zoneIds = zoneIds;
    }

    public void addZoneId(Integer zoneId) {
        zoneIds.add(zoneId);
    }

    public Integer getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
    }

    public Integer getFlightId() {
        return flightId;
    }

    public void setFlightId(Integer flightId) {
        this.flightId = flightId;
    }

    public Integer getAdId() {
        return adId;
    }

    public void setAdId(Integer adId) {
        this.adId = adId;
    }

    public URL getClickUrl() {
        return clickUrl;
    }

    public void setClickUrl(URL clickUrl) {
        this.clickUrl = clickUrl;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public void addProperty(Property property) {
        properties.add(property);
    }

    public List<EventId> getEventIds() {
        return eventIds;
    }

    public void setEventIds(List<EventId> eventIds) {
        this.eventIds = eventIds;
    }

    public void addEventId(EventId eventId) {
        eventIds.add(eventId);
    }
}

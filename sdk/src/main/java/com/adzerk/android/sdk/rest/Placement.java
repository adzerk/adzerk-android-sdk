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
    private String mDivName;

    // network id to use when selecting an ad (required)
    private Long mNetworkId;

    // site id to use when selecting an ad (required)
    private Long mSiteId;

    // one or more integer ad types to use when selecting an ad (required)
    private List<Integer> mAdTypes;

    // zero or more zone ids to use when selecting an ad
    private List<Integer> mZoneIds;

    // campaign id; if specified, only consider ads in that campaign
    private Integer mCampaignId;

    // flight id; if specified, only consider ads in that flight
    private Integer mFlightId;

    // ad (flight-creative map) id; if specified, only serve that ad if possible
    private Integer mAdId;

    // URL that should be used as the click-through target for the ad
    private URL mClickUrl;

    // hash of key/value pairs used for custom targeting
    private List<Property> mProperties;

    // array of numeric event types. Requests tracking URLs for custom events
    private List<EventId> mEventIds;

    public String getDivName() {
        return mDivName;
    }

    public void setDivName(String divName) {
        mDivName = divName;
    }

    public Long getNetworkId() {
        return mNetworkId;
    }

    public void setNetworkId(Long networkId) {
        mNetworkId = networkId;
    }

    public Long getSiteId() {
        return mSiteId;
    }

    public void setSiteId(Long siteId) {
        mSiteId = siteId;
    }

    public List<Integer> getAdTypes() {
        return mAdTypes;
    }

    public void setAdTypes(List<Integer> adTypes) {
        mAdTypes = adTypes;
    }

    public void addAdType(Integer adType) {
        mAdTypes.add(adType);
    }

    public List<Integer> getZoneIds() {
        return mZoneIds;
    }

    public void setZoneIds(List<Integer> zoneIds) {
        mZoneIds = zoneIds;
    }

    public void addZoneId(Integer zoneId) {
        mZoneIds.add(zoneId);
    }

    public Integer getCampaignId() {
        return mCampaignId;
    }

    public void setCampaignId(Integer campaignId) {
        mCampaignId = campaignId;
    }

    public Integer getFlightId() {
        return mFlightId;
    }

    public void setFlightId(Integer flightId) {
        mFlightId = flightId;
    }

    public Integer getAdId() {
        return mAdId;
    }

    public void setAdId(Integer adId) {
        mAdId = adId;
    }

    public URL getClickUrl() {
        return mClickUrl;
    }

    public void setClickUrl(URL clickUrl) {
        mClickUrl = clickUrl;
    }

    public List<Property> getProperties() {
        return mProperties;
    }

    public void setProperties(List<Property> properties) {
        mProperties = properties;
    }

    public void addProperty(Property property) {
        mProperties.add(property);
    }

    public List<EventId> getEventIds() {
        return mEventIds;
    }

    public void setEventIds(List<EventId> eventIds) {
        mEventIds = eventIds;
    }

    public void addEventId(EventId eventId) {
        mEventIds.add(eventId);
    }
}

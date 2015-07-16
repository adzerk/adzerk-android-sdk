package com.adzerk.android.sdk.rest;

import java.net.URL;
import java.util.List;

/**
 * A Decision represents the ad that was selected to be served for a given Placement.
 *
 * A Response will contain zero or more Decisions, one per Placement that was sent in on the request.
 * If no ad was selected for a given Placement, the corresponding Decision entry will be undefined (null).
 */
public class Decision {

    // id for the ad that was selected
    private Integer mAdId;

    // id for the creative in the selected ad
    private Integer mCreativeId;

    // id for the flight in the selected ad
    private Integer mFlightId;

    // id for the campaign in the selected ad
    private Integer mCampaignId;

    // url endpoint that, using a GET, triggers the recording of the click and redirects to the target
    private URL mClickUrl;

    // list of ad contents
    private List<Content> mContents;

    // list if Events - the IDs and tracking URLs of custom events
    private List<Event> mEvents;

    // url endpoint that, using a GET, triggers the recording of the impression
    private URL mImpressionUrl;

    public Integer getAdId() {
        return mAdId;
    }

    public void setAdId(Integer adId) {
        mAdId = adId;
    }

    public Integer getCreativeId() {
        return mCreativeId;
    }

    public void setCreativeId(Integer creativeId) {
        mCreativeId = creativeId;
    }

    public Integer getFlightId() {
        return mFlightId;
    }

    public void setFlightId(Integer flightId) {
        mFlightId = flightId;
    }

    public Integer getCampaignId() {
        return mCampaignId;
    }

    public void setCampaignId(Integer campaignId) {
        mCampaignId = campaignId;
    }

    public URL getClickUrl() {
        return mClickUrl;
    }

    public void setClickUrl(URL clickUrl) {
        mClickUrl = clickUrl;
    }

    public List<Content> getContents() {
        return mContents;
    }

    public void setContents(List<Content> contents) {
        mContents = contents;
    }

    public void addContent(Content content) {
        mContents.add(content);
    }

    public URL getImpressionUrl() {
        return mImpressionUrl;
    }

    public void setImpressionUrl(URL impressionUrl) {
        mImpressionUrl = impressionUrl;
    }

    public List<Event> getEvents() {
        return mEvents;
    }

    public void setEvents(List<Event> events) {
        mEvents = events;
    }

    public void addEvent(Event eventId) {
        mEvents.add(eventId);
    }
}

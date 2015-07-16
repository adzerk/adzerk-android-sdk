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
    private Integer adId;

    // id for the creative in the selected ad
    private Integer creativeId;

    // id for the flight in the selected ad
    private Integer flightId;

    // id for the campaign in the selected ad
    private Integer campaignId;

    // url endpoint that, using a GET, triggers the recording of the click and redirects to the target
    private URL clickUrl;

    // list of ad contents
    private List<Content> contents;

    // list if Events - the IDs and tracking URLs of custom events
    private List<Event> events;

    private int height;

    private int width;

    // url endpoint that, using a GET, triggers the recording of the impression
    private URL impressionUrl;

    public Integer getAdId() {
        return adId;
    }

    public void setAdId(Integer adId) {
        this.adId = adId;
    }

    public Integer getCreativeId() {
        return creativeId;
    }

    public void setCreativeId(Integer creativeId) {
        this.creativeId = creativeId;
    }

    public Integer getFlightId() {
        return flightId;
    }

    public void setFlightId(Integer flightId) {
        this.flightId = flightId;
    }

    public Integer getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
    }

    public URL getClickUrl() {
        return clickUrl;
    }

    public void setClickUrl(URL clickUrl) {
        this.clickUrl = clickUrl;
    }

    public List<Content> getContents() {
        return contents;
    }

    public void setContents(List<Content> contents) {
        this.contents = contents;
    }

    public void addContent(Content content) {
        contents.add(content);
    }

    public URL getImpressionUrl() {
        return impressionUrl;
    }

    public void setImpressionUrl(URL impressionUrl) {
        this.impressionUrl = impressionUrl;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void addEvent(Event eventId) {
        events.add(eventId);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}

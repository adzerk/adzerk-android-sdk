package com.adzerk.android.sdk.rest;

import java.util.List;

/**
 * A Decision represents the ad that was selected to be served for a given Placement.
 *
 * A Response will contain zero or more Decisions, one per Placement that was sent in on the request.
 * If no ad was selected for a given Placement, the corresponding Decision entry will be undefined (null).
 */
public class Decision {

    // id for the ad that was selected
    int adId;

    // id for the creative in the selected ad
    int creativeId;

    // id for the flight in the selected ad
    int flightId;

    // id for the campaign in the selected ad
    int campaignId;

    // url endpoint that, using a GET, triggers the recording of the click and redirects to the target
    String clickUrl;

    // list of ad contents
    List<Content> contents;

    // list if Events - the IDs and tracking URLs of custom events
    List<Event> events;

    // url endpoint that, using a GET, triggers the recording of the impression
    String impressionUrl;

    public int getAdId() {
        return adId;
    }

    public int getCreativeId() {
        return creativeId;
    }

    public int getFlightId() {
        return flightId;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public String getClickUrl() {
        return clickUrl;
    }

    public List<Content> getContents() {
        return contents;
    }

    public String getImpressionUrl() {
        return impressionUrl;
    }

    public List<Event> getEvents() {
        return events;
    }

}

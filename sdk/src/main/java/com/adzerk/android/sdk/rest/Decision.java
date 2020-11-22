package com.adzerk.android.sdk.rest;

import android.location.Location;

import com.adzerk.android.sdk.gson.MatchedPointsDeserializer;
import com.google.gson.annotations.JsonAdapter;

import java.util.List;

/**
 * A Decision represents the ad that was selected to be served for a given {@link Placement}.
 * <p>
 * A {@link DecisionResponse} will contain zero or more Decisions, one per Placement that was sent in on the requestPlacement.
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

    @JsonAdapter(MatchedPointsDeserializer.class)
    List<Location> matchedPoints;

    /**
     * Returns id for the ad that was selected
     * @return ad id
     */
    public int getAdId() {
        return adId;
    }

    /**
     * Returns id for the creative in the selected ad
     * @return creative id
     */
    public int getCreativeId() {
        return creativeId;
    }

    /**
     * Returns id for the flight in the selected ad
     * @return flight id
     */
    public int getFlightId() {
        return flightId;
    }

    /**
     * Returns id for the campaign in the selected ad
     * @return campaign id
     */
    public int getCampaignId() {
        return campaignId;
    }

    /**
     * Returns url endpoint that, using a GET, triggers the recording of the click and redirects to the target
     * @return url to record clicks
     */
    public String getClickUrl() {
        return clickUrl;
    }

    /**
     * Returns the list of {@link Content}s; the creatives needed to render the ad.
     * @return contents
     */
    public List<Content> getContents() {
        return contents;
    }

    /**
     * Returns url endpoint that, using a GET, triggers the recording of the impression
     * @return url to record ad impression
     */
    public String getImpressionUrl() {
        return impressionUrl;
    }

    /**
     * Returns list of {@link Event}s; the IDs and tracking URLs of custom events
     * @return event list
     */
    public List<Event> getEvents() {
        return events;
    }

    public List<Location> getMatchedPoints() {
        return matchedPoints;
    }
}

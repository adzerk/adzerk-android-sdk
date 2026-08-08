package com.adzerk.android.sdk.rest;

import android.location.Location;

import com.adzerk.android.sdk.gson.MatchedPointsDeserializer;
import com.google.gson.annotations.JsonAdapter;

import java.util.List;
import java.util.Map;

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

    // id for the advertiser in the selected ad
    int advertiserId;

    // url endpoint that, using a GET, triggers the recording of the click and redirects to the target
    String clickUrl;

    // list of ad contents
    List<Content> contents;

    // list if Events - the IDs and tracking URLs of custom events
    List<Event> events;

    // url endpoint that, using a GET, triggers the recording of the impression
    String impressionUrl;

    // height of the selected ad, if the creative supplies it
    Integer height;

    // width of the selected ad, if the creative supplies it
    Integer width;

    // custom metadata configured on the ad; only present if set
    Map<String, Object> externalMetadata;

    // ecpm partition of the matched impression; only present if set
    String ecpmPartition;

    // when multiple ads are selected for a non-multi-winner placement, the ads beyond the first
    List<Decision> adChain;

    @JsonAdapter(MatchedPointsDeserializer.class)
    List<Location> matchedPoints;

    // pricing details; only present when the Request sets includePricingData to true
    PricingData pricing;

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
     * Returns id for the advertiser in the selected ad
     * @return advertiser id
     */
    public int getAdvertiserId() {
        return advertiserId;
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

    /**
     * Returns the height of the selected ad, or null if the creative does not supply one
     * @return ad height
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * Returns the width of the selected ad, or null if the creative does not supply one
     * @return ad width
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * Returns the custom metadata configured on the ad, or null if none is set
     * @return map of custom metadata
     */
    public Map<String, Object> getExternalMetadata() {
        return externalMetadata;
    }

    /**
     * Returns the ecpm partition of the matched impression, or null if none is set
     * @return ecpm partition
     */
    public String getEcpmPartition() {
        return ecpmPartition;
    }

    /**
     * Returns the additional ads beyond the first when multiple ads were selected for a
     * non-multi-winner {@link Placement}, or null if there are none
     * @return list of additional decisions
     */
    public List<Decision> getAdChain() {
        return adChain;
    }

    public List<Location> getMatchedPoints() {
        return matchedPoints;
    }

    /**
     * Returns the {@link PricingData} for the selected ad, or null if the {@link Request} did not
     * set the includePricingData option
     * @return pricing details
     */
    public PricingData getPricing() {
        return pricing;
    }
}

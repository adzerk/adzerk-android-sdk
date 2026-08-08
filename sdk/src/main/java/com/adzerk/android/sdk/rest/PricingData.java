package com.adzerk.android.sdk.rest;

import com.google.gson.annotations.SerializedName;

/**
 * Pricing details for the ad that was selected for a {@link Placement}.
 * <p>
 * Only present when the ad {@link Request} sets the includePricingData option to true, and
 * individual fields are only present when they apply to the matched impression.
 *
 * @see Decision
 */
public class PricingData {

    // price of the impression
    Float price;

    // price the impression cleared at
    Float clearPrice;

    // only present when a bid modifier applied to the impression
    Float modifiedPrice;

    // only present when the flight has a targetROAS configured
    Float optimizedPrice;

    // multiplier applied to the value of the impression's events
    Float eventMultiplier;

    // revenue recorded for the impression
    Float revenue;

    // rate type of the flight that served the impression
    Integer rateType;

    // effective cost per thousand impressions
    @SerializedName("eCPM")
    Float eCPM;

    /**
     * Returns the price of the impression, or null if not present
     * @return price
     */
    public Float getPrice() {
        return price;
    }

    /**
     * Returns the price the impression cleared at, or null if not present
     * @return clear price
     */
    public Float getClearPrice() {
        return clearPrice;
    }

    /**
     * Returns the modified price, only present when a bid modifier applied to the impression
     * @return modified price
     */
    public Float getModifiedPrice() {
        return modifiedPrice;
    }

    /**
     * Returns the optimized price, only present when the flight has a targetROAS configured
     * @return optimized price
     */
    public Float getOptimizedPrice() {
        return optimizedPrice;
    }

    /**
     * Returns the multiplier applied to the value of the impression's events, or null if not present
     * @return event multiplier
     */
    public Float getEventMultiplier() {
        return eventMultiplier;
    }

    /**
     * Returns the revenue recorded for the impression, or null if not present
     * @return revenue
     */
    public Float getRevenue() {
        return revenue;
    }

    /**
     * Returns the rate type of the flight that served the impression, or null if not present
     * @return rate type
     */
    public Integer getRateType() {
        return rateType;
    }

    /**
     * Returns the effective cost per thousand impressions, or null if not present
     * @return eCPM
     */
    public Float getECPM() {
        return eCPM;
    }
}

package com.adzerk.android.sdk.rest;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * To create a Request for ads, use {@link Request.Builder} to build the ad request.
 * <p>
 * Every Request must contain one or more Placements. Each placement represents a "slot" in which an ad may be served.
 * By specifying multiple placements, you can request multiple ads in a single call.
 * <p>
 * <pre>
 * {@code
 * // Get the SDK
 * AdzerkSdk sdk = AdzerkSdk.getInstance();
 *
 * // Build the Request
 * Request request = new Request.Builder()
 *     .addPlacement(new Placement(name1, networkId, siteId, adTypes))
 *     .addPlacement(new Placement(name2, networkId, siteId, adTypes))
 *     .build();
 *
 * // Issue the Request
 * sdk.request(request, listener);
 * }
 * </pre>
 * @see com.adzerk.android.sdk.rest.Request.Builder
 * @see com.adzerk.android.sdk.AdzerkSdk
 */
public class Request {

    // list of placements where an ad can be served (required)
    ArrayList<Placement> placements;

    // target to the user key used to identify a unique user
    User user;

    // zero or more keywords to use when selecting the ad
    Set<String> keywords;

    // URL to use as the referrer when selecting an ad
    String referrer;

    // URL to use as the current page URL when selecting an ad
    String url;

    // UNIX epoch timestamp to use when selecting an ad
    Long time;

    // IP address to use when selecting the ad; if specified, overrides the IP the request is made from
    String ip;

    // zero or more numeric creative ids to disregard when selecting an ad
    Set<Integer> blockedCreatives;

    // hash of flight ids to arrays of UNIX epoch timestamps representing times the user viewed an ad in the specified flight (used for frequency capping)
    Map<Integer, List<Long>> flightViewTimes;

    /**
     * Builder to configure a Request for ads.
     * <p>
     * Multiple ads can be served by a single Request. Placements identify locations in the app where an ad can
     * be served and multiple placements can be sent with a Request. And each placement specifies its own parameters
     * for selecting an ad.
     * <p>
     * <pre>
     * {@code
     * // Using Builder to create a Request:
     * Request request = new Builder()
     *       .addPlacement(new Placement("div1", 1L, 2L, 5))
     *       .addPlacement(new Placement("div2", 1L, 2L, 5))
     *       .setUser(new User(key))
     *       .addKeywords("sports")
     *       .setUrl("http://adzerk.com")
     *       .setFlightViewTimes(1, time1, time2, time3)
     *       .build();
     * }
     * </pre>
     */
    public static class Builder {

        private ArrayList<Placement> placements;
        private User user;
        private Set<String> keywords;
        private String referrer;
        private String url;
        private Long time;
        private String ip;
        private Set<Integer> blockedCreatives;
        private Map<Integer, List<Long>> flightViewTimes;


        /**
         * Builder to configure a Request for ads.
         */
        public Builder() {
            this.placements = new ArrayList<>();
        }

        /**
         * Builder to configure a Request containing the specified list of Placements.
         * <p>
         * <pre>
         * {@code
         * // Create list of Placements
         * List<Placement> placements = new ArrayList<>();
         * placements.add(new Placement("div1", 1L, 2L, 5));
         * placements.add(new Placement("div1", 1L, 2L, 5));
         *
         * // Building a Request:
         * Request request = Request.Builder(placements).build();
         * }
         * </pre>
         *
         * @param placements list of placements where an ad can be served (required)
         */
        public Builder(@NonNull List<Placement> placements) {
            if (placements.isEmpty()) {
                throw new IllegalArgumentException("At least one Placement must be specified");
            }
            this.placements = new ArrayList<>(placements);
        }

        /**
         * Add Placement to Request
         *
         * @param placement identifies where and ad can be served
         * @return request builder
         */
        public Builder addPlacement(Placement placement) {
            this.placements.add(placement);

            return this;
        }

        /**
         * User to target. If the request doesn't contain a user key, one will be automatically generated
         * in the response.
         *
         * @param user  user identified by a unique key
         * @return request builder
         */
        public Builder setUser(User user) {
            this.user = user;
            return this;
        }

        /**
         * Keywords used when selecting the ad
         *
         * @param keywords  zero or more keywords
         * @return request builder
         */
        public Builder setKeywords(Set<String> keywords) {
            this.keywords = keywords;
            return this;
        }

        /**
         * Add keywords to the list of keywords used when selecting adds
         *
         * @param keywords keywords to add
         * @return
         */
        public Builder addKeywords(String... keywords) {
            if (this.keywords == null) {
                this.keywords = new HashSet<String>();
            }
            for (String keyword : keywords) {
                this.keywords.add(keyword);
            }

            return this;
        }

        /**
         * URL used as referrer when selecting an ad
         *
         * @param referrer  referrer URL
         * @return request builder
         */
        public Builder setReferrer(String referrer) {
            this.referrer = referrer;
            return this;
        }

        /**
         * URL to use as the current page URL when selecting an ad
         *
         * @param url   URL of current page
         * @return request builder
         */
        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        /**
         * Timestamp to use when selecting an ad
         *
         * @param time  UNIX epoch timestamp
         * @return request builder
         */
        public Builder setTime(long time) {
            this.time = time;
            return this;
        }

        /**
         * IP address to use when selecting the ad. If specified, overrides the IP the request is made from
         *
         * @param ip    ip address
         * @return request builder
         */
        public Builder setIp(String ip) {
            this.ip = ip;
            return this;
        }

        /**
         * Creative ids to disregard when selecting an ad
         *
         * @param blockedCreatives  zero or more numeric creative ids to disregard
         * @return request builder
         */
        public Builder setBlockedCreatives(Set<Integer> blockedCreatives) {
            this.blockedCreatives = blockedCreatives;
            return this;
        }

        /**
         * Add creative ids to blocked list
         *
         * @param blockedCreatives creative ids to add to blocked list
         * @return request builder
         */
        public Builder addBlockedCreatives(int... blockedCreatives) {
            if (this.blockedCreatives == null) {
                this.blockedCreatives = new HashSet<Integer>();
            }
            for (int blockedCreative : blockedCreatives) {
                this.blockedCreatives.add(blockedCreative);
            }

            return this;
        }

        /**
         * Sets a list of times the user viewed an ad in the specified flight. Used for frequency capping.
         *
         * Note: If flight view times were previously specified for a flight id they will be overwritten.
         *
         * @param flightId          id of flight to set view times
         * @param flightViewTimes   list of UNIX epoch timestamps
         * @return request builder
         */
        public Builder setFlightViewTimes(int flightId, long... flightViewTimes) {
            List<Long> flightViewTimesList =  new ArrayList<Long>();
            for (long flightViewTime : flightViewTimes ) {
                flightViewTimesList.add(flightViewTime);
            }

            if (this.flightViewTimes == null) {
                this.flightViewTimes = new HashMap<Integer, List<Long>>();
            }
            this.flightViewTimes.put(flightId, flightViewTimesList);
            return this;
        }

        /**
         * Create the Request
         *
         * @return ad request
         */
        public Request build() {
            if (placements.isEmpty()) {
                throw new IllegalStateException("At least one Placement must be specified");
            }
            return new Request(this);
        }

    }

    // end: Request.Builder

    private Request(Builder builder) {
        setPlacements(builder.placements);
        setUser(builder.user);
        setKeywords(builder.keywords);
        setReferrer(builder.referrer);
        setUrl(builder.url);
        if (builder.time != null)
            setTime(builder.time);
        setIp(builder.ip);
        setBlockedCreatives(builder.blockedCreatives);
        setAllFlightViewTimes(builder.flightViewTimes);
    }

    /**
     * Returns list of placements where an ad can be served
     *
     * @return  placements
     */
    public List<Placement> getPlacements() {
        return placements;
    }

    private void setPlacements(ArrayList<Placement> placements) {
        this.placements = placements;
    }

    /**
     *  Returns the User key used to identify a unique user
     *
     * @return user
     */
    public User getUser() {
        return user;
    }

    private void setUser(User user) {
        this.user = user;
    }

    /**
     * Returns the keywords to use when selecting the ad
     *
     * @return
     */
    public Set<String> getKeywords() {
        return keywords;
    }

    private void setKeywords(Set<String> keywords) {
        this.keywords = keywords;
    }

    /**
     * Returns the URL to use as the referrer when selecting an ad
     *
     * @return
     */
    public String getReferrer() {
        return referrer;
    }

    private void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    /**
     *  Return the URL to used for the current page when selecting an ad
     *
     * @return url
     */
    public String getUrl() {
        return url;
    }

    private void setUrl(String url) {
        this.url = url;
    }

    /**
     * Returns the UNIX epoch timestamp to use when selecting an ad
     *
     * @return  epoch timestamp
     */
    public long getTime() {
        return time;
    }

    private void setTime(long epochTime) {
        time = epochTime;
    }

    /**
     * The IP address to use when selecting the ad; if specified, overrides the IP the request is made from
     *
     * @return ip address
     */
    public String getIp() {
        return ip;
    }

    private void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * Returns set of numeric creative ids to disregard when selecting an ad
     *
     * @return  ids of creatives to disregard
     */
    public Set<Integer> getBlockedCreatives() {
        return blockedCreatives;
    }

    private void setBlockedCreatives(Set<Integer> blockedCreatives) {
        this.blockedCreatives = blockedCreatives;
    }

    /**
     * Returns map of flight ids to arrays of UNIX epoch timestamps representing times the user viewed an ad in
     * the specified flight (used for frequency capping)
     *
     * @return  flight view times to cap ad frequency
     */
    public Map<Integer, List<Long>> getAllFlightViewTimes() {
        return flightViewTimes;
    }

    /**
     * Returns the UNIX epoch timestamps representing times the user viewed an ad in the specified flight
     * (used for frequency capping)
     *
     * @return  flight view times to cap ad frequency
     */
    public List<Long> getFlightViewTimes(int flightId) {
        if (flightViewTimes != null && flightViewTimes.containsKey(flightId)) {
            return flightViewTimes.get(flightId);
        }

        return Collections.<Long>emptyList();
    }

    private void setAllFlightViewTimes(Map<Integer, List<Long>> flightViewTimes) {
        this.flightViewTimes = flightViewTimes;
    }
}

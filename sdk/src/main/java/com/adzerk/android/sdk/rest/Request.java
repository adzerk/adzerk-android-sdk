package com.adzerk.android.sdk.rest;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A Request for Ads.
 *
 * Every Request must contain one or more Placements. Each placement represents a "slot" in which an ad may be served.
 */
public class Request {

    // list of placements where an ad can be served (required)
    private List<Placement> placements;

    // target to the user key used to identify a unique user
    private User user;

    // zero or more keywords to use when selecting the ad
    private Set<String> keywords;

    // URL to use as the referrer when selecting an ad
    private String referrer;

    // URL to use as the current page URL when selecting an ad
    private String url;

    // UNIX epoch timestamp to use when selecting an ad
    private long time;

    // IP address to use when selecting the ad; if specified, overrides the IP the request is made from
    private String ip;

    // zero or more numeric creative ids to disregard when selecting an ad
    private Set<Integer> blockedCreatives;

    // hash of flight ids to arrays of UNIX epoch timestamps representing times the user viewed an ad in the specified flight (used for frequency capping)
    private Map<Integer, List<Long>> flightViewTimes;

    // if true, only ads containing a single image will be returned (defaults to false)
    private boolean isMobile = Boolean.FALSE;

    /**
     * Builder used to create Request for ads.
     */
    public static class Builder {

        private List<Placement> placements;
        private User user;
        private Set<String> keywords;
        private String referrer;
        private String url;
        private long time;
        private String ip;
        private Set<Integer> blockedCreatives;
        private Map<Integer, List<Long>> flightViewTimes;
        private boolean isMobile = false;

        /**
         * Builder to configure a Request for Ads.
         *
         * Multiple ads can be served by a single Request. Placements identify locations in the app where an ad can
         * be served. And each placement defines separate properties for a requested ad.
         *
         * <pre>
         * {@code
         * Request request = Request.Builder(placements)
         *     .setUser(user)
         *     .setMobile(true)
         *     .build()
         * }
         * </pre>
         *
         * @param placements list of placements where an ad can be served (required)
         */
        public Builder(@NonNull List<Placement> placements) {
            if (placements.isEmpty()) {
                throw new IllegalArgumentException("At least one Placement must be specified");
            }
            this.placements = placements;
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
         * Add a keyword
         *
         * @param keyword keyword to add
         * @return
         */
        public Builder addKeyword(String keyword) {
            if (keywords == null) {
                this.keywords = new HashSet<String>();
            }
            keywords.add(keyword);

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
         * @param url   URL of current page
         * @return request builder
         */
        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        /**
         * Timestamp to use when selecting an ad
         * @param time  UNIX epoch timestamp
         * @return request builder
         */
        public Builder setTime(long time) {
            this.time = time;
            return this;
        }

        /**
         * IP address to use when selecting the ad. If specified, overrides the IP the request is made from
         * @param ip    ip address
         * @return request builder
         */
        public Builder setIp(String ip) {
            this.ip = ip;
            return this;
        }

        /**
         * Creative ids to disregard when selecting an ad
         * @param blockedCreatives  zero or more numeric creative ids to disregard
         * @return request builder
         */
        public Builder setBlockedCreatives(Set<Integer> blockedCreatives) {
            this.blockedCreatives = blockedCreatives;
            return this;
        }

        /**
         * Add a creative id to blocked list
         *
         * @param blockedCreative creative id to add to blocked list
         * @return
         */
        public Builder addBlockedCreative(int blockedCreative) {
            if (this.blockedCreatives == null) {
                this.blockedCreatives = new HashSet<Integer>();
            }
            this.blockedCreatives.add(blockedCreative);

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
        public Builder setFlightViewTimes(int flightId, @NonNull List<Long> flightViewTimes) {
            if (this.flightViewTimes == null) {
                this.flightViewTimes = new HashMap<Integer, List<Long>>();
            }
            this.flightViewTimes.put(flightId, flightViewTimes);
            return this;
        }

        /**
         * If true, only ads containing a single image will be returned (defaults to false)
         *
         * @param isMobile  set to true to request single image ads
         * @return request builder
         */
        public Builder setMobile(boolean isMobile) {
            this.isMobile = isMobile;
            return this;
        }

        /**
         * Create the Request
         *
         * @return ad request
         */
        public Request build() {
            return new Request(this);
        }

    }

    private Request(Builder builder) {
        setPlacements(builder.placements);
        setUser(builder.user);
        setKeywords(builder.keywords);
        setReferrer(builder.referrer);
        setUrl(builder.url);
        setTime(builder.time);
        setIp(builder.ip);
        setBlockedCreatives(builder.blockedCreatives);
        setAllFlightViewTimes(builder.flightViewTimes);
        setMobile(builder.isMobile);
    }


    public List<Placement> getPlacements() {
        return placements;
    }

    private void setPlacements(List<Placement> placements) {
        this.placements = placements;
    }

    public void addPlacement(Placement placement) {
        placements.add(placement);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(Set<String> keywords) {
        this.keywords = keywords;
    }

    public void addKeyword(String keyword) {
        keywords.add(keyword);
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long epochTime) {
        time = epochTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Set<Integer> getBlockedCreatives() {
        return blockedCreatives;
    }

    public void setBlockedCreatives(Set<Integer> blockedCreatives) {
        this.blockedCreatives = blockedCreatives;
    }

    public void addBlockedCreative(int creativeId) {
        blockedCreatives.add(creativeId);
    }

    public Map<Integer, List<Long>> getAllFlightViewTimes() {
        return flightViewTimes;
    }

    public List<Long> getFlightViewTimes(int flightId) {
        if (flightViewTimes.containsKey(flightId))
            return flightViewTimes.get(flightId);

        return null;
    }

    public void setAllFlightViewTimes(Map<Integer, List<Long>> flightViewTimes) {
        this.flightViewTimes = flightViewTimes;
    }

    public void setFlightViewTimes(int flightId, List<Long> flightViewTimes) {
        if (this.flightViewTimes == null) {
            this.flightViewTimes = new HashMap<Integer, List<Long>>();
        }
        this.flightViewTimes.put(flightId, flightViewTimes);
    }

    public boolean isMobile() {
        return isMobile;
    }

    public void setMobile(boolean isMobile) {
        this.isMobile = isMobile;
    }
}

package com.adzerk.android.sdk.rest;

import java.net.InetAddress;
import java.net.URL;
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
    private List<Placement> mPlacements;

    // target to the user key used to identify a unique user
    private User mUser;

    // zero or more keywords to use when selecting the ad
    private Set<String> mKeywords;

    // URL to use as the referrer when selecting an ad
    private URL mReferrer;

    // URL to use as the current page URL when selecting an ad
    private URL mUrl;

    // UNIX epoch timestamp to use when selecting an ad
    private Long mTime;

    // IP address to use when selecting the ad; if specified, overrides the IP the request is made from
    private InetAddress mIp;

    // zero or more numeric creative ids to disregard when selecting an ad
    private Set<Integer> mBlockedCreatives;

    // hash of flight ids to arrays of UNIX epoch timestamps representing times the user viewed an ad in the specified flight (used for frequency capping)
    private Map<Integer, List<Integer>> mFlightViewTimes;

    // if true, only ads containing a single image will be returned (defaults to false)
    private Boolean mIsMobile = Boolean.FALSE;

    /**
     * Builder used to create Request for ads.
     */
    public static class Builder {

        private List<Placement> placements;
        private User user;
        private Set<String> keywords;
        private URL referrer;
        private URL url;
        private Long time;
        private InetAddress ip;
        private Set<Integer> blockedCreatives;
        private Map<Integer, List<Integer>> flightViewTimes;
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
        public Builder(List<Placement> placements) {
            if (placements == null || placements.isEmpty())
                throw new IllegalArgumentException("At least one Placement must be specified");
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
         * URL used as referrer when selecting an ad
         *
         * @param referrer  referrer URL
         * @return request builder
         */
        public Builder setReferrer(URL referrer) {
            this.referrer = referrer;
            return this;
        }

        /**
         * URL to use as the current page URL when selecting an ad
         * @param url   URL of current page
         * @return request builder
         */
        public Builder setUrl(URL url) {
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
        public Builder setIP(InetAddress ip) {
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
         * Map of flight ids to list of timestamps representing times the user viewed an ad in the specified
         * flight. Used for frequency capping.
         *
         * @param flightViewTimes   map of flight ids to list of UNIX epoch timestamps
         * @return request builder
         */
        public Builder setFlightViewTimes(Map<Integer, List<Integer>> flightViewTimes) {
            this.flightViewTimes = flightViewTimes;
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
    }


    public List<Placement> getPlacements() {
        return mPlacements;
    }

    private void setPlacements(List<Placement> placements) {
        mPlacements = placements;
    }

    public void addPlacement(Placement placement) {
        mPlacements.add(placement);
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public Set<String> getKeywords() {
        return mKeywords;
    }

    public void setKeywords(Set<String> keywords) {
        mKeywords = keywords;
    }

    public void addKeyword(String keyword) {
        mKeywords.add(keyword);
    }

    public URL getReferrer() {
        return mReferrer;
    }

    public void setReferrer(URL referrer) {
        mReferrer = referrer;
    }

    public URL getUrl() {
        return mUrl;
    }

    public void setUrl(URL url) {
        mUrl = url;
    }

    public Long getTime() {
        return mTime;
    }

    public void setTime(Long epochTime) {
        mTime = epochTime;
    }

    public InetAddress getIp() {
        return mIp;
    }

    public void setIp(InetAddress ip) {
        mIp = ip;
    }

    public Set<Integer> getBlockedCreatives() {
        return mBlockedCreatives;
    }

    public void setBlockedCreatives(Set<Integer> blockedCreatives) {
        mBlockedCreatives = blockedCreatives;
    }

    public void addBlockedCreative(Integer creativeId) {
        mBlockedCreatives.add(creativeId);
    }

    public Map<Integer, List<Integer>> getFlightViewTimes() {
        return mFlightViewTimes;
    }

    public void setFlightViewTimes(Map<Integer, List<Integer>> flightViewTimes) {
        mFlightViewTimes = flightViewTimes;
    }

    public Boolean isMobile() {
        return mIsMobile;
    }

    public void setMobile(Boolean isMobile) {
        mIsMobile = isMobile;
    }
}

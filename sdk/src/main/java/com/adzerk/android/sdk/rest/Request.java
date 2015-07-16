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

    // list pf placements where an ad can be served (required)
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
    private Boolean mIsMobile;

    public List<Placement> getPlacements() {
        return mPlacements;
    }

    public void setPlacements(List<Placement> placements) {
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

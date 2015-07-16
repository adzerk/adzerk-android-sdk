package com.adzerk.android.sdk.rest;

import java.util.List;

/**
 * Response to an ad Request.
 *
 * A Response will contain zero or more Decisions, one per Placement that was sent in on the request. If no ad was
 * selected for a given Placement, the corresponding Decision entry will be undefined (null).
 */
public class Response {

    // identifies the unique user that places the request
    private User mUser;

    // each Decision represents the ad that was selected to be served for a given Placement
    private List<Decision> mDecisions;

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public List<Decision> getDecisions() {
        return mDecisions;
    }

    public Decision getDecision(String divName) {
        // TODO: assuming we want to query by the divName
        return null;
    }

    public void setDecisions(List<Decision> decisions) {
        mDecisions = decisions;
    }

    public void addDecision(Decision decision) {
        mDecisions.add(decision);
    }
}

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
    private User user;

    // each Decision represents the ad that was selected to be served for a given Placement
    private List<Decision> decisions;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Decision> getDecisions() {
        return decisions;
    }

    public Decision getDecision(String divName) {
        // TODO: assuming we want to query by the divName
        return null;
    }

    public void setDecisions(List<Decision> decisions) {
        this.decisions = decisions;
    }

    public void addDecision(Decision decision) {
        decisions.add(decision);
    }
}

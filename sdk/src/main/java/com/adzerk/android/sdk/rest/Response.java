package com.adzerk.android.sdk.rest;

import java.util.Map;

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
    private Map<String, Decision> decisions;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Map<String, Decision> getDecisions() {
        return decisions;
    }

    public Decision getDecision(String name) {
        return decisions.get(name);
    }

    public void setDecisions(Map<String, Decision> decisions) {
        this.decisions = decisions;
    }

    public void addDecision(String name, Decision decision) {
        decisions.put(name, decision);
    }
}

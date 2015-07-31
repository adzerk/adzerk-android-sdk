package com.adzerk.android.sdk.rest;

import java.util.Map;

/**
 * The DecisionResponse to an ad {@link Request}.
 * <p>
 * A DecisionResponse will contain zero or more {@link Decision}s, one per {@link Placement} that was sent in on the requestPlacement.
 * If no ad was selected for a given Placement, the corresponding Decision entry will be undefined (null).
 */
public class DecisionResponse {

    // identifies the unique user that places the requestPlacement
    User user;

    // each Decision represents the ad that was selected to be served for a given Placement
    Map<String, Decision> decisions;

    /**
     * Returns the User key which uniquely identifies the user that places the requestPlacement
     * @return user key
     */
    public User getUser() {
        return user;
    }

    /**
     * Returns mapping from {@link Placement} name to the {@link Decision} that represents the ad selected to be served
     * @return map of decisions by placement name
     */
    public Map<String, Decision> getDecisions() {
        return decisions;
    }

    /**
     * Returns the {@link Decision} by name
     * @return decison for specified placement name
     */
    public Decision getDecision(String name) {
        return decisions.get(name);
    }

}

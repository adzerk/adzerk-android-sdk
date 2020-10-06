package com.adzerk.android.sdk.rest;

import com.adzerk.android.sdk.gson.DecisionsDeserializer;
import com.google.gson.annotations.JsonAdapter;

import java.util.List;
import java.util.Map;

/**
 * The DecisionResponse to an ad {@link Request}.
 * <p>
 * A DecisionResponse will contain zero or more {@link Decision}s per {@link Placement} that was sent in on the requestPlacement.
 * If no ad was selected for a given Placement, the corresponding entry will be undefined (null).
 */
public class DecisionResponse {

    // identifies the unique user that places the requestPlacement
    User user;

    // each Decision represents an ad that was selected to be served for a given Placement
    @JsonAdapter(DecisionsDeserializer.class)
    Map<String, List<Decision>> decisions;

    /**
     * Returns the User key which uniquely identifies the user that places the requestPlacement
     * @return user key
     */
    public User getUser() {
        return user;
    }

    /**
     * Returns mapping from {@link Placement} name to the list of {@link Decision}s that represents the ad selected to be served
     * @return map of selected decisions by placement name
     */
    public Map<String, List<Decision>> getDecisions() {
        return decisions;
    }

    /**
     * Returns the list of {@link Decision}s by Placement name
     * @return one or more decisions for specified placement name
     */
    public List<Decision> getDecisions(String name) {
        return decisions.get(name);
    }

}

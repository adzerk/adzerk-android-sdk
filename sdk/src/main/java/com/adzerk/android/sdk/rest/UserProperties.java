package com.adzerk.android.sdk.rest;

import com.google.gson.JsonObject;

import java.util.Map;

/**
 * Contains the custom properties parse from the User object. Not intended for API consumers.
 * @see User
 */
public class UserProperties {

    // map of user custom properties as key-value pairs
    Map<String, Object> customProperties;

    // raw JSON object; useful for clients needing custom deserialization
    JsonObject customPropertiesJson;


    public UserProperties(Map<String, Object> customProperties, JsonObject customPropertiesJson) {
        this.customProperties = customProperties;
        this.customPropertiesJson = customPropertiesJson;
    }

    /**
     * Returns TRUE if custom properties have been defined
     * @return true if there are properties defined
     */
    boolean hasCustomProperties() {
        return customProperties != null && !customProperties.isEmpty();
    }

    /**
     * Returns map of custom properties
     * @return map of key-value pairs
     */
    Map<String, Object> getCustomProperties() {
        return customProperties;
    }

    /**
     * Returns custom property value for the specified key
     * @return map of key-value pairs
     */
    Object getCustomProperty(String key) {
        if (hasCustomProperties() && customProperties.containsKey(key)) {
            return customProperties.get(key);
        }
        return null;
    }

    /**
     * Returns the custom properties as a JsonObject.
     * @return json object representation of the custom properties
     */
    JsonObject getCustomPropertiesJson() {
        return customPropertiesJson;
    }

    /**
     * Convenience method that returns the custom properties JSON as a String
     * @return json string or null
     */
    String getCustomPropertiesAsString() {
        if (customPropertiesJson != null) {
            return customPropertiesJson.toString();
        }
        return null;
    }
}

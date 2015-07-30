package com.adzerk.android.sdk.rest;

import com.google.gson.JsonObject;

import java.util.Collections;
import java.util.Map;

/**
 * Contains the data and metadata parsed from a Content element. Not intended for API consumers.
 * @see Content
 */
public class ContentData {

    // data key for the json metadata
    static String KEY_CUSTOM_DATA = "customData";

    // data key for image url
    static String KEY_IMAGE_URL = "imageUrl";

    // data key for title
    static String KEY_TITLE = "title";

    // map of creative data (title, height, width, etc) used to build the content body
    Map<String, Object> creativeData;

    // map of creative metadata; the deserialized JSON metadata from the creative
    Map<String, Object> creativeMetadata;

    // raw creative metadata JSON object; useful for clients needing custom deserialization
    JsonObject creativeMetadataJson;


    public ContentData(Map<String, Object> creativeData, JsonObject creativeMetadataJson) {
        this.creativeData = creativeData;
        this.creativeMetadataJson = creativeMetadataJson;
    }

    /**
     * Returns TRUE if content contains creativeData
     * @return true if creativeData is not empty
     */
    boolean hasCreativeData() {
        return creativeData != null && !creativeData.isEmpty();
    }

    /**
     * Returns creativeData object that has fields used to build the content
     * @return map of key-value pairs
     */
    Map<String, Object> getCreativeData() {
        return creativeData;
    }

    /**
     * Returns creativeData object that has fields used to build the content
     * @return map of key-value pairs
     */
    Object getCreativeData(String key) {
        if (hasCreativeData() && creativeData.containsKey(key)) {
            return creativeData.get(key);
        }
        return null;
    }

    /**
     * Creatives may specify optional metadata in JSON format. This method returns that JSON metadata deserialized
     * as Java Obects.
     * <p>
     * If the creative data contains JSON metadata, it may be used by itself or together the content 'body'
     * to render the ad.
     * @return JSON metadata content
     */
    Map<String, Object> getCreativeMetadata() {

        if (hasCreativeData() && creativeData.containsKey(KEY_CUSTOM_DATA)) {
            Object creativeMetadata = creativeData.get(KEY_CUSTOM_DATA);
            if (creativeMetadata instanceof Map) {
                return (Map) creativeMetadata;
            }
        }
        return Collections.EMPTY_MAP;
    }

    /**
     * Returns a value from the JSON metadata content set by the creative.
     * @param key   JSON attribute name
     * @return  object value or null
     */
    Object getCreativeMetadata(String key) {
        Map<String, Object> creativeMetadata = getCreativeMetadata();
        if (!creativeMetadata.isEmpty() && creativeMetadata.containsKey(key)) {
            return creativeMetadata.get(key);
        }
        return null;
    }

    /**
     * Returns the creative metadata as a JsonObject.
     * @return json object containing metadata for the creative or null
     */
    JsonObject getCreativeMetadataAsJson() {
        return creativeMetadataJson;
    }

    /**
     * Convenience method that returns the creative metadata as a String of JSON.
     * @return json string or null
     */
    String getCreativeMetadataAsString() {
        if (creativeMetadataJson != null) {
            return creativeMetadataJson.toString();
        }
        return null;
    }

    /**
     * Returns the value of the 'imageUrl' from creativeData
     * @return url of image or null
     */
    String getImageUrl() {
        return (getCreativeData(KEY_IMAGE_URL) != null) ? creativeData.get(KEY_IMAGE_URL).toString() : null;
    }

    /**
     * Returns the value of the 'title' from creativeData
     * @return ad title or null
     */
    String getTitle() {
        return (getCreativeData(KEY_TITLE) != null) ? creativeData.get(KEY_TITLE).toString() : null;
    }

}

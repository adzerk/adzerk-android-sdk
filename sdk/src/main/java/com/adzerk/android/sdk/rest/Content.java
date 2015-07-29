package com.adzerk.android.sdk.rest;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.Map;

/**
 * Each {@link Decision} contains one or more Contents. Combined, the Contents represent the creative that should
 * be displayed.
 * <p>
 * For example, a creative may contain a CSS stylesheet and a block of HTML. This would be represented as
 * two Contents, one with the type css and one with the type html.
 * <p>
 * Custom metadata set at the creative level will be passed in the Contents as the key customData.
 * <p>
 * If a content uses a predefined template, the template property will be set to the name of the template to use.
 * For example, an image content will have the {@link Content#TYPE_HTML} and the {@link Content#TEMPLATE_IMAGE}.
 * <p>
 * @see Decision
 */
public class Content {

    public static String TYPE_HTML = "html";
    public static String TYPE_CSS = "css";
    public static String TYPE_JS = "js";
    public static String TYPE_JS_EXTERNAL = "js-external";
    public static String TYPE_RAW = "raw";

    public static String TEMPLATE_IMAGE = "image";
    public static String TEMPLATE_IMAGE_NO_WIDTH = "image-nowidth";
    public static String TEMPLATE_FLASH = "flash";
    public static String TEMPLATE_FLASH_NO_WIDTH = "flash-nowidth";

    static String KEY_IMAGE_URL = "imageUrl";
    static String KEY_TITLE = "title";
    static String KEY_CUSTOM_DATA = "customData";


    // the type of the content
    String type;

    // name of the template used to render the content (unless TYPE_RAW)
    String template;

    // the body of the custom template for TYPE_RAW content
    String customTemplate;

    // rendered body of the content
    String body;

    // object that has fields used to build the content from. Includes the creative title, height, width, etc.
    @SerializedName("data")
    Map<String, Object> creativeData;

    // contains JSON metadata set at the creative level
    // TODO: add support for retrieving the raw JSON either as a String or possibly a JsonObject/JsonElement (gson)
    String creativeMetadata;

    /**
     * Returns type of the content: {@link Content#TYPE_HTML}, {@link Content#TYPE_CSS}, {@link Content#TYPE_JS},
     * {@link Content#TYPE_JS_EXTERNAL}, {@link Content#TYPE_RAW}
     * @return content type
     */
    public String getType() {
        return type;
    }

    /**
     * Returns name of the template used to render the content (unless {@link Content#TEMPLATE_IMAGE})
     * @return name of template
     */
    public String getTemplate() {
        return template;
    }

    /**
     * Returns body of the custom template for {@link Content#TYPE_RAW} content
     * @return body of custom template
     */
    public String getCustomTemplate() {
        return customTemplate;
    }

    /**
     * Returns rendered body of the content
     * @return  content body
     */
    public String getBody() {
        return body;
    }

    /**
     * Returns TRUE if content contains creativeData
     * @return true if creativeData is not empty
     */
    public boolean hasCreativeData() {
        return creativeData != null && !creativeData.isEmpty();
    }

    /**
     * Returns creativeData object that has fields used to build the content
     * @return map of key-value pairs
     */
    public Map<String, Object> getCreativeData() {
        return creativeData;
    }

    /**
     * Returns creativeData object that has fields used to build the content
     * @return map of key-value pairs
     */
    public Object getCreativeData(String key) {
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
    public Map<String, Object> getCreativeMetadata() {

        if (hasCreativeData() && creativeData.containsKey(KEY_CUSTOM_DATA)) {
            Object creativeMetadata = creativeData.get(KEY_CUSTOM_DATA);
            if (creativeMetadata instanceof Map) {
                return (Map) creativeMetadata;
            }
        }
        return Collections.EMPTY_MAP;
    }

    /**
     * Returns the JSON string holding metadata specified by the creative.
     * @return string of JSON
     */
    public String getCreativeMetadataAsJson() {
        return creativeMetadata;
    }

    /**
     * Returns a value from the JSON metadata content set by the creative.
     * @param key   JSON attribute name
     * @return  object value or null
     */
    public Object getCreativeMetadata(String key) {
        Map<String, Object> creativeMetadata = getCreativeMetadata();
        if (!creativeMetadata.isEmpty() && creativeMetadata.containsKey(key)) {
            return creativeMetadata.get(key);
        }
        return null;
    }

    /**
     * Returns TRUE if content is {@link Content#TYPE_RAW}
     * @return true if raw content type
     */
    public boolean isRawType() {
        return TYPE_RAW.equals(getType());
    }

    /**
     * Returns TRUE if template is {@link Content#TEMPLATE_IMAGE}
     * @return true if template is image
     */
    public boolean isImage() {
        return TEMPLATE_IMAGE.equals(getTemplate());
    }

    /**
     * Returns the value of the 'imageUrl' from creativeData
     * @return url of image or null
     */
    public String getImageUrl() {
        return (getCreativeData(KEY_IMAGE_URL) != null) ? creativeData.get(KEY_IMAGE_URL).toString() : null;
    }

    /**
     * Returns the value of the 'title' from creativeData
     * @return ad title or null
     */
    public String getTitle() {
        return (getCreativeData(KEY_TITLE) != null) ? creativeData.get(KEY_TITLE).toString() : null;
    }
}

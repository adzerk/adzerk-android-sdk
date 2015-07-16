package com.adzerk.android.sdk.rest;

import java.util.Map;

/**
 * Content - Each Decision contains one or more Contents. Combined, the Contents represent the creative that should
 * be displayed. For example, a creative may contain a CSS stylesheet and a block of HTML. This would be represented as
 * two Contents, one with the type css and one with the type html.
 *
 * Custom metadata set at the creative level will be passed in the Contents as the key customData.
 *
 * If a content uses a predefined template, the template property will be set to the name of the template to use.
 * For example, an image content will have the type html and the template image.
 */
public class Content {

    public static final String TYPE_HTML = "html";
    public static final String TYPE_CSS = "css";
    public static final String TYPE_JS = "js";
    public static final String TYPE_JS_EXTERNAL = "js-external";
    public static final String TYPE_RAW = "raw";

    public static final String TEMPLATE_IMAGE = "image";
    public static final String TEMPLATE_IMAGE_NO_WIDTH = "image-nowidth";
    public static final String TEMPLATE_FLASH = "flash";
    public static final String TEMPLATE_FLASH_NO_WIDTH = "flash-nowidth";

    // the type of the content
    private String mType;

    // name of the template used to render the content (unless TYPE_RAW)
    private String mTemplate;

    // the body of the custom template for TYPE_RAW content
    private String mCustomTemplate;

    // rendered body of the content
    private String mBody;

    // data An object that has fields used to build the content
    private Map<String, String> mData;


    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getTemplate() {
        return mTemplate;
    }

    public void setTemplate(String template) {
        mTemplate = template;
    }

    public String getCustomTemplate() {
        return mCustomTemplate;
    }

    public void setCustomTemplate(String customTemplate) {
        this.mCustomTemplate = customTemplate;
    }

    public String getBody() {
        return mBody;
    }

    public void setBody(String body) {
        mBody = body;
    }

    public Map<String, String> getData() {
        return mData;
    }

    public void setData(Map<String, String> data) {
        this.mData = data;
    }

    public boolean isRawType() {
        return mType == TYPE_RAW;
    }
}

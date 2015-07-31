package com.adzerk.android.sdk.rest;

/**
 * Event information included in DecisionResponse to an ad requestPlacement. Once you call an Event URL, Adzerk will track that
 * event for the creative/campaign/advertiser etc. in Adzerk reporting.
 * <p/>
 * Events in Adzerk are data about a way a user interacts with an ad. Standard events included with all Adzerk
 * accounts are clicks, impressions, and conversions. Custom events are defined by the user of the account.
 */
public class Event {

    public static final int ID_VIEW_CONVERSION = 1;
    public static final int ID_CLICK_CONVERSION = 2;
    public static final int ID_SERVER_CONVERSION = 3;
    public static final int ID_UPVOTE = 10;
    public static final int ID_DOWNVOTE = 11;
    public static final int ID_DOWNVOTE_UNINTERESTING = 12;
    public static final int ID_DOWNVOTE_MISLEADING = 13;
    public static final int ID_DOWNVOTE_OFFENSIVE = 14;
    public static final int ID_DOWNVOTE_REPETITIVE = 15;
    public static final int ID_LIKE = 20;
    public static final int ID_SHARE = 21;
    public static final int ID_COMMENT = 22;
    public static final int ID_VISIBLE = 30;
    public static final int ID_HOVER = 31;
    public static final int ID_EXPAND = 32;
    public static final int ID_SHARE_FACEBOOK = 50;
    public static final int ID_SHARE_TWITTER = 51;
    public static final int ID_SHARE_PINTEREST = 52;
    public static final int ID_SHARE_REDDIT = 53;
    public static final int ID_SHARE_EMAIL = 54;
    public static final int ID_START = 70;
    public static final int ID_FIRST_QUARTILE = 71;
    public static final int ID_MIDPOINT = 72;
    public static final int ID_THIRD_QUARTILE = 73;
    public static final int ID_COMPLETE = 74;
    public static final int ID_MUTE = 75;
    public static final int ID_UNMUTE = 76;
    public static final int ID_PAUSE = 77;
    public static final int ID_REWIND = 78;
    public static final int ID_RESUME = 79;
    public static final int ID_FULLSCREEN = 80;
    public static final int ID_EXIT_FULLSCREEN = 81;
    public static final int ID_EXPAND_VIDEO = 82;
    public static final int ID_COLLAPSE = 83;
    public static final int ID_ACCEPT_INVITATION_LINEAR = 84;
    public static final int ID_CLOSE_LINEAR = 85;
    public static final int ID_SKIP = 86;
    public static final int ID_PROGRESS = 87;
    public static final int ID_COMMENT_REPLY = 101;
    public static final int ID_COMMENT_UPVOTE = 102;
    public static final int ID_COMMENT_DOWNVOTE = 103;
    public static final int ID_CUSTOM_01 = 104;
    public static final int ID_CUSTOM_02 = 105;
    public static final int ID_CUSTOM_03 = 106;
    public static final int ID_CUSTOM_04 = 107;
    public static final int ID_CUSTOM_05 = 108;
    public static final int ID_CUSTOM_06 = 109;
    public static final int ID_CUSTOM_07 = 110;


    // event identifier
    int eventId;

    // url to call to track an event
    String eventUrl;

    /**
     * Returns an event identifier
     * @return numeric event id
     */
    public int getEventId() {
        return eventId;
    }

    /**
     * Returns tracking URL of custom event
     * @return tracking url
     */
    public String getEventUrl() {
        return eventUrl;
    }

}

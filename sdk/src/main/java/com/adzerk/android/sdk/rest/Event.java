package com.adzerk.android.sdk.rest;

import java.net.URL;

/**
 * Event information included in Response to an ad request. Once you call an Event URL, Adzerk will track that
 * event for the creative/campaign/advertiser etc. in Adzerk reporting.
 *
 * Events in Adzerk are data about a way a user interacts with an ad. Standard events included with all Adzerk
 * accounts are clicks, impressions, and conversions. Custom events are defined by the user of the account.
 */
public class Event {

    // event identifier
    private EventId eventId;

    // url to call to track an event
    private URL eventUrl;

    public EventId getEventId() {
        return eventId;
    }

    public void setEventId(EventId eventId) {
        this.eventId = eventId;
    }

    public URL getEventUrl() {
        return eventUrl;
    }

    public void setEventUrl(URL eventUrl) {
        this.eventUrl = eventUrl;
    }
}

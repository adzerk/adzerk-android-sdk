package com.adzerk.android.sdk.rest;

/**
 * Sets data consent preferences.
 *
 * For example, "consent": {"gdpr": true} sets GDPR consent for tracking in the European Union.
 *
 * @see Decision
 */
public class Consent {

    // consent for tracking in the EU
    boolean gdpr = false;

    public Consent(boolean gdpr) {
        this.gdpr = gdpr;
    }

    public boolean isGdpr() {
        return gdpr;
    }

    public void setGdpr(boolean gdpr) {
        this.gdpr = gdpr;
    }
}

package com.adzerk.android.sdk.rest;

import com.adzerk.android.sdk.AdzerkSdk;
import com.adzerk.android.sdk.rest.Request.Builder;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.Assert.fail;

public class RequestTest {

    AdzerkSdk sdk;

    @Mock NativeAdService api;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        sdk = AdzerkSdk.getInstance(api);
    }

    @Test
    public void itShouldThrowOnEmptyPlacements() {
        try {
            new Builder(new ArrayList<Placement>()).build();
            fail("Failed to throw on empty placements");
        } catch(IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void itShouldBuildUser() {
        //TODO: Use the builder to create a request and verify contents
    }

    @Test
    public void itShouldBuildKeywords() {

    }

    @Test
    public void itShouldAddKeywords() {

    }

    @Test
    public void itShouldBuildReferrer() {

    }

    @Test
    public void itShouldBuildUrl() {

    }

    // TODO: Remaining tests for Request and Builder coverage
    // Some may logically go together, so they can be aggregated.
}
package com.adzerk.android.sdk.rest;

import com.adzerk.android.sdk.AdzerkSdk;
import com.adzerk.android.sdk.BuildConfig;
import com.adzerk.android.sdk.MockClient;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(sdk=25, constants=BuildConfig.class)
public class FirePixelResponseTest {

    AdzerkSdk sdk;
    MockClient mockClient;

    @Before
    public void setUp() throws Exception {
        mockClient = new MockClient("");
        mockClient.addResponseHeader("location", "clickTargetUrl");
        sdk = AdzerkSdk.createInstance(mockClient.buildClient());
    }

    @Test
    public void itShouldCreatePixelResponse() {

        String clickUrl = "http://engine.adzerk.net/clicked";
        FirePixelResponse response = sdk.firePixelSynchronous(clickUrl, null, null, null);

        assertThat(response).isNotNull();
        assertEquals(response.location, "clickTargetUrl");
        assertEquals(response.statusCode, 200);
    }

    @Test
    public void itShouldCreatePixelResponseFor302() {
        mockClient.setResponseCode(302, "redirect test");
        String clickUrl = "http://engine.adzerk.net/clicked";
        FirePixelResponse response = sdk.firePixelSynchronous(clickUrl, null, null, null);

        assertThat(response).isNotNull();
        assertEquals(response.location, "clickTargetUrl");
        assertEquals(response.statusCode, 302);
    }

    @Test
    public void itShouldSupportRevenueModifier() {

        String clickUrl = "http://engine.adzerk.net/clicked";
        FirePixelResponse response = sdk.firePixelSynchronous(clickUrl, 1.75f, AdzerkSdk.RevenueModifierType.OVERRIDE, null);

        assertThat(response).isNotNull();
        assertEquals(response.location, "clickTargetUrl");
        assertEquals(response.statusCode, 200);
    }
}
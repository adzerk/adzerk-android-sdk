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

@RunWith(RobolectricTestRunner.class)
@Config(sdk=25, constants=BuildConfig.class)
public class UserResponseTest {

    AdzerkSdk sdk;
    MockClient mockClient;

    static String userKey = "ue1-d720342a233c4631a58dfb6b54f43480";
    static long networkId = 9792L;

    @Before
    public void setUp() throws Exception {
        mockClient = new MockClient(JSON_USER);
        sdk = AdzerkSdk.createInstance(mockClient.buildClient());
    }

    @Test
    public void itShouldDeserializeUser() {
        User user = sdk.readUserSynchronous(networkId, userKey);
        assertThat(user).isNotNull();
        assertThat(user.getKey()).isEqualTo(userKey);

        assertThat(user.getCustomProperty("age")).isEqualTo(new Double(28));
        assertThat(user.getCustomProperty("gender")).isEqualTo("male");
        assertThat(user.hasInterest("cats"));
        assertThat(user.isNew()).isFalse();
    }
    
    static final String JSON_USER = "{" +
          "    \"adViewTimes\": {}, " +
          "    \"blockedItems\": {" +
          "        \"advertisers\": [], " +
          "        \"campaigns\": [], " +
          "        \"creatives\": [], " +
          "        \"flights\": []" +
          "    }, " +
          "    \"cookieMonster\": {}, " +
          "    \"custom\": {" +
          "        \"age\": 28, " +
          "        \"gender\": \"male\"" +
          "    }, " +
          "    \"dirtyCookies\": {}, " +
          "    \"flightViewTimes\": {}, " +
          "    \"interests\": [" +
          "        \"[sausage,bacon]\", " +
          "        \"cats\", " +
          "        \"dogs\", " +
          "        \"pancakes\", " +
          "        \"ponies\"" +
          "    ], " +
          "    \"isNew\": false, " +
          "    \"key\": \"ue1-d720342a233c4631a58dfb6b54f43480\", " +
          "    \"optOut\": false, " +
          "    \"partnerUserIds\": {}, " +
          "    \"pendingConversions\": [], " +
          "    \"retargetingSegments\": {}, " +
          "    \"siteViewTimes\": {}" +
          "}";


}
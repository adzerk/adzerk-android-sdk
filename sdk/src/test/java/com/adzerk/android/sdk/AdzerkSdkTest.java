package com.adzerk.android.sdk;

import com.adzerk.android.sdk.rest.AdzerkService;
import com.adzerk.android.sdk.rest.DecisionResponse;
import com.adzerk.android.sdk.rest.Placement;
import com.adzerk.android.sdk.rest.Request;
import com.adzerk.android.sdk.rest.User;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.mime.TypedInput;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(sdk=18, constants=BuildConfig.class, manifest = Config.NONE)
public class AdzerkSdkTest {

    AdzerkSdk sdk;

    @Mock AdzerkService api;

    static String userKey = "ue1-d720342a233c4631a58dfb6b54f43480";
    static long networkId = 9792L;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        sdk = AdzerkSdk.createInstance(api);
    }

    @Test
    public void itShouldRequestNativeAd() {
        Request request = createTestRequest();
        sdk.requestPlacement(request, null);
        verify(api).request(argThat(new RequestMatcher(request)), (Callback<DecisionResponse>) any());
    }

    @Test
    public void itShouldRefuseMalformedUrls() {
        assertThat(sdk.impression("this is wrong")).isFalse();
    }

    @Test
    public void itShouldSetUserPropertiesFromMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("age", 33);
        map.put("gender", "female");

        try {
            sdk.setUserProperties(networkId, userKey, map, null);
            verify(api).postUserProperties(eq(networkId), eq(userKey), argThat(new IsMapWithSameContents(map)), (ResponseCallback) any());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void itShouldSetUserPropertiesFromJson() {

        try {
            sdk.setUserProperties(networkId, userKey, userProperties, null);
            TypedInput jsonInput = sdk.createTypedJsonString(userProperties);
            verify(api).postUserProperties(eq(networkId), eq(userKey), eq(jsonInput), (ResponseCallback) any());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void itShouldCallReadUser() {

        try {
            sdk.readUser(networkId, userKey, null);
            verify(api).readUser(eq(networkId), eq(userKey), (Callback<User>) any());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void itShouldSetUserInterest() {

        String interest = "ponies";
        try {
            sdk.setUserInterest(networkId, userKey, interest, null);
            verify(api).setUserInterest(eq(networkId), eq(userKey), eq(interest), (ResponseCallback) any());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void itShouldSetUserOptout() {

        try {
            sdk.setUserOptout(networkId, userKey, null);
            verify(api).setUserOptout(eq(networkId), eq(userKey), (ResponseCallback) any());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void itShouldSetRetargetting() {
        long brandId = 999L;
        String segment = "boomers";
        try {
            sdk.setUserRetargeting(networkId, brandId, segment, userKey, null);
            verify(api).setUserRetargeting(eq(networkId), eq(brandId), eq(segment), eq(userKey), (ResponseCallback) any());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private Request createTestRequest() {
        String divName = "div1";
        long networkId = 9709;
        long siteId = 70464;

        List<Placement> placements = Arrays.asList(new Placement(divName, networkId, siteId, 5));
        return new Request.Builder(placements).build();
    }

    private static String userProperties = "{ \"age\": 28, \"gender\": \"male\" }";

    static class RequestMatcher extends ArgumentMatcher<Request> {

        Request expected;

        public RequestMatcher(Request expected) {
            this.expected = expected;
        }

        @Override
        public boolean matches(Object actual) {
            return actual == expected;
        }
    }

    class IsMapWithSameContents extends ArgumentMatcher<Map<String, Object>> {

        Map<String, Object> expected;

        public IsMapWithSameContents(Map<String, Object> expected) {
            this.expected = expected;
        }

        @Override
        public boolean matches(Object map) {
            return expected.equals(map);
        }
    }
}

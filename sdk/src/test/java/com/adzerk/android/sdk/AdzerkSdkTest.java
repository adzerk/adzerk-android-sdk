package com.adzerk.android.sdk;

import com.adzerk.android.sdk.rest.AdzerkService;
import com.adzerk.android.sdk.rest.DecisionResponse;
import com.adzerk.android.sdk.rest.Placement;
import com.adzerk.android.sdk.rest.Request;
import com.adzerk.android.sdk.rest.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Call;
import retrofit2.Response;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(sdk=25, constants=BuildConfig.class)
public class AdzerkSdkTest {

    AdzerkSdk sdk;
    MockWebServer mockWebServer;

    @Mock AdzerkService api;

    @Mock Call<DecisionResponse> mockRequestCall;
    @Mock Call<User> mockUserCall;
    @Mock Call<Void> mockVoidCall;

    @Mock User mockUser;
    @Mock DecisionResponse mockDecisionResponse;

    @Mock AdzerkSdk.UserListener mockUserListener;
    @Mock AdzerkSdk.DecisionListener mockDecisionListener;

    static String userKey = "ue1-d720342a233c4631a58dfb6b54f43480";
    static long networkId = 9792L;

    @Before
    public void setup() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        MockitoAnnotations.initMocks(this);
        sdk = AdzerkSdk.createInstance(api);
    }

    @After
    public void teardown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void itShouldAddSdkVersionHeader() throws Exception {
        mockWebServer.enqueue(new MockResponse());

        AdzerkSdk sdk = new AdzerkSdk.Builder().networkId(23L).hostname(mockWebServer.getHostName() + ":" + mockWebServer.getPort()).protocol("http").build();
        Request request = createTestRequest();
        sdk.requestPlacementSynchronous(request);

        RecordedRequest httpRequest = mockWebServer.takeRequest();
        assertEquals("adzerk-android-sdk:" + BuildConfig.VERSION_NAME, httpRequest.getHeader("X-Adzerk-Sdk-Version"));
    }

    @Test
    public void itShouldSetDefaultNetworkIdForPlacement() throws Exception {
        mockWebServer.enqueue(new MockResponse());

        AdzerkSdk sdk = new AdzerkSdk.Builder().networkId(23L).hostname(mockWebServer.getHostName() + ":" + mockWebServer.getPort()).protocol("http").build();
        List<Placement> placements = Arrays.asList(new Placement("div0", 70464L, 5));
        Request request = new Request.Builder(placements).build();
        sdk.requestPlacementSynchronous(request);

        RecordedRequest httpRequest = mockWebServer.takeRequest();
        assertThat(httpRequest.getBody().toString().contains("\"networkId\":23"));
    }

    @Test
    public void itShouldRequestNativeAd() {
        try {
            Request request = createTestRequest();
            when(api.request(request)).thenReturn(mockRequestCall);
            doAnswerDecisionResponse();
            sdk.requestPlacement(request, mockDecisionListener);
            verify(mockDecisionListener, times(1)).success(mockDecisionResponse);
            verify(api).request(argThat(new RequestMatcher(request)));
        } catch (Exception e) {
            fail(e.getMessage());
        }
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
            when(api.postUserProperties(networkId, userKey, map)).thenReturn(mockVoidCall);
            doAnswerVoidResponse();
            sdk.setUserProperties(networkId, userKey, map, mockUserListener);
            verify(mockUserListener, times(1)).success(null);
            verify(api).postUserProperties(eq(networkId), eq(userKey), argThat(new IsMapWithSameContents(map)));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void itShouldSetUserPropertiesFromJson() {

        try {
            when(api.postUserProperties(eq(networkId), eq(userKey), any(RequestBody.class))).thenReturn(mockVoidCall);
            doAnswerVoidResponse();
            sdk.setUserProperties(networkId, userKey, userProperties, mockUserListener);
            verify(mockUserListener, times(1)).success(null);
            verify(api).postUserProperties(eq(networkId), eq(userKey), any(RequestBody.class));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void itShouldCallReadUser() {

        try {
            when(api.readUser(networkId, userKey)).thenReturn(mockUserCall);
            doAnswerUserResponse();
            sdk.readUser(networkId, userKey, mockUserListener);
            verify(mockUserListener, times(1)).success(any(User.class));
            verify(api).readUser(eq(networkId), eq(userKey));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void itShouldSetUserInterest() {

        String interest = "ponies";
        try {
            when(api.setUserInterest(networkId, userKey, interest)).thenReturn(mockVoidCall);
            doAnswerVoidResponse();
            sdk.setUserInterest(networkId, userKey, interest, mockUserListener);
            verify(mockUserListener, times(1)).success(null);
            verify(api).setUserInterest(eq(networkId), eq(userKey), eq(interest));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void itShouldSetUserOptout() {
        try {
            when(api.setUserOptout(networkId, userKey)).thenReturn(mockVoidCall);
            doAnswerVoidResponse();
            sdk.setUserOptout(networkId, userKey, mockUserListener);
            verify(mockUserListener, times(1)).success(null);
            verify(api).setUserOptout(eq(networkId), eq(userKey));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void itShouldSetRetargetting() {
        long brandId = 999L;
        String segment = "boomers";
        try {
            when(api.setUserRetargeting(networkId, brandId, segment, userKey)).thenReturn(mockVoidCall);
            doAnswerVoidResponse();
            sdk.setUserRetargeting(networkId, brandId, segment, userKey, mockUserListener);
            verify(mockUserListener, times(1)).success(null);
            verify(api).setUserRetargeting(eq(networkId), eq(brandId), eq(segment), eq(userKey));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void itShouldCallUserListenerOnSuccessWithNull() {
        try {
            Response<User> response = Response.success(null);
            AdzerkSdk.AdzerkCallback<User, Void> callback = new AdzerkSdk.AdzerkCallback("UserListenerSuccess", mockUserListener);
            callback.onResponse(mockUserCall, response);
            verify(mockUserListener, times(1)).success(null);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void itShouldCallUserListenerSuccessWithUser() {
        try {
            Response<User> realResponse = Response.success(mock(User.class));
            AdzerkSdk.AdzerkCallback<User, User> callback = new AdzerkSdk.AdzerkCallback("UserListenerSuccess", mockUserListener);
            callback.onResponse(mockUserCall, realResponse);
            verify(mockUserListener, times(1)).success(any(User.class));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void itShouldCallUserListenerFail() {
        try {
            AdzerkSdk.AdzerkCallback<User, User> callback = new AdzerkSdk.AdzerkCallback("UserListenerFail", mockUserListener);
            callback.onFailure(mockUserCall, mock(Exception.class));
            verify(mockUserListener, times(1)).error(any(AdzerkSdk.AdzerkError.class));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void builderShouldRequireNetworkId() {
        exceptionRule.expect(IllegalStateException.class);
        exceptionRule.expectMessage("A networkId is required");
        new AdzerkSdk.Builder().build();
    }

    @Test
    public void builderShouldOverrideHostname() {
        AdzerkSdk sdk = new AdzerkSdk.Builder().networkId(23L).hostname("myhost.acme.com").build();
        assertTrue(sdk.baseUrl.contains("myhost.acme.com"));
    }

    @Test
    public void builderShouldSetDefaultNetworkId() {
        AdzerkSdk sdk = new AdzerkSdk.Builder().networkId(23L).build();
        assertTrue(sdk.defaultNetworkId == 23L);
    }

    @Test
    public void builderShouldSetEDashHostname() {
        AdzerkSdk sdk = new AdzerkSdk.Builder().networkId(23L).build();
        assertTrue(sdk.baseUrl.equals("https://e-23.adzerk.net"));
    }

    private Request createTestRequest() {
        String divName = "div1";
        long networkId = 9709;
        long siteId = 70464;

        List<Placement> placements = Arrays.asList(new Placement(divName, networkId, siteId, 5));
        return new Request.Builder(placements).build();
    }

    // mock Void api response
    private void doAnswerVoidResponse() {
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                AdzerkSdk.AdzerkCallback callback = invocation.getArgument(0);
                callback.onResponse(mockVoidCall, Response.success(null));
                return null;
            }
        }).when(mockVoidCall).enqueue(any(AdzerkSdk.AdzerkCallback.class));
    }

    // mock User api response
    private void doAnswerUserResponse() {
        doAnswer(new Answer<User>() {
            @Override
            public User answer(InvocationOnMock invocation) throws Throwable {
                AdzerkSdk.AdzerkCallback callback = invocation.getArgument(0);
                callback.onResponse(mockUserCall, Response.success(mockUser));
                return null;
            }
        }).when(mockUserCall).enqueue(any(AdzerkSdk.AdzerkCallback.class));
    }

    // mock Decision api response
    private void doAnswerDecisionResponse() {
        doAnswer(new Answer<DecisionResponse>() {
            @Override
            public DecisionResponse answer(InvocationOnMock invocation) throws Throwable {
                AdzerkSdk.AdzerkCallback callback = invocation.getArgument(0);
                callback.onResponse(mockUserCall, Response.success(mockDecisionResponse));
                return null;
            }
        }).when(mockRequestCall).enqueue(any(AdzerkSdk.AdzerkCallback.class));
    }

    private static String userProperties = "{ \"age\": 28, \"gender\": \"male\" }";

    static class RequestMatcher implements ArgumentMatcher<Request> {

        Request expected;

        public RequestMatcher(Request expected) {
            this.expected = expected;
        }

        @Override
        public boolean matches(Request argument) {
            return argument == expected;
        }
    }

    class IsMapWithSameContents implements ArgumentMatcher<Map<String, Object>> {

        Map<String, Object> expected;

        public IsMapWithSameContents(Map<String, Object> expected) {
            this.expected = expected;
        }

        @Override
        public boolean matches(Map<String, Object> map) {
            return expected.equals(map);
        }
    }
}

package com.adzerk.android.sdk;

import com.adzerk.android.sdk.AdzerkSdk.ResponseListener;
import com.adzerk.android.sdk.rest.NativeAdService;
import com.adzerk.android.sdk.rest.Placement;
import com.adzerk.android.sdk.rest.Request;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(sdk=18, constants=BuildConfig.class, manifest = Config.NONE)
public class AdzerkSdkTest {

    AdzerkSdk sdk;

    @Mock NativeAdService api;
    @Captor ArgumentCaptor<ResponseListener> responseListener;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        sdk = AdzerkSdk.getInstance(api);
    }

    @Test
    public void itShouldRequestNativeAd() {
        Request request = createTestRequest();
        sdk.request(request, null);
        verify(api).request(request);
    }

    private Request createTestRequest() {
        String divName = "div1";
        long networkId = 9709;
        long siteId = 70464;

        List<Placement> placements = Arrays.asList(new Placement(divName, networkId, siteId, Arrays.asList(5)));
        return new Request.Builder(placements).build();
    }
}

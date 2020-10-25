package com.adzerk.android.sdk.gson;


import android.location.Location;

import com.adzerk.android.sdk.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.JsonAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(sdk=25, constants= BuildConfig.class)
public class MatchedPointsDeserializerTest {

    public class MatchedPointsContainer {
        @JsonAdapter(MatchedPointsDeserializer.class)
        List<Location> matchedPoints;
    }

    Gson gson;

    @Before
    public void setUp() throws Exception {
        gson = new GsonBuilder().create();
    }

    @Test
    public void ShouldSucceed_WhenJsonIsValidMatchedPoints() {
        MatchedPointsContainer result = gson.fromJson(JSON_VALID_MATCHEDPOINTS, MatchedPointsContainer.class);

        // Decisions
        assertThat(result).isNotNull();
        assertThat(result.matchedPoints).isNotNull().isNotEmpty().hasSize(3);
        assertThat(result.matchedPoints.get(0).getLatitude()).isEqualTo(35.995063);
        assertThat(result.matchedPoints.get(0).getLongitude()).isEqualTo(-78.908187);
        assertThat(result.matchedPoints.get(1).getLatitude()).isEqualTo(40.689188);
        assertThat(result.matchedPoints.get(1).getLongitude()).isEqualTo(-74.044562);
        assertThat(result.matchedPoints.get(2).getLatitude()).isEqualTo(29.979188);
        assertThat(result.matchedPoints.get(2).getLongitude()).isEqualTo(31.134188);
    }

    @Test(expected = JsonParseException.class)
    public void ShouldThrow_WhenJsonIsNotArray() {
       gson.fromJson(JSON_INVALID_1, MatchedPointsContainer.class);
    }

    @Test(expected = JsonParseException.class)
    public void ShouldThrow_WhenLatLonAreNotStrings() {
        gson.fromJson(JSON_INVALID_2, MatchedPointsContainer.class);
    }

    @Test(expected = JsonParseException.class)
    public void ShouldThrow_WhenLatLonStringsAreNotParsableAsDoubles() {
        gson.fromJson(JSON_INVALID_3, MatchedPointsContainer.class);
    }

    @Test(expected = JsonParseException.class)
    public void ShouldThrow_WhenLatLonMissing() {
        gson.fromJson(JSON_INVALID_4, MatchedPointsContainer.class);
    }

    static String JSON_VALID_MATCHEDPOINTS =
            " {\"matchedPoints\": [" +
            "    {" +
            "       \"lat\": \"35.995063\"," +
            "       \"lon\": \"-78.908187\"" +
            "     }," +
            "     {" +
            "       \"lat\": \"40.689188\"," +
            "       \"lon\": \"-74.044562\"" +
            "     }," +
            "     {" +
            "        \"lat\": \"29.979188\"," +
            "        \"lon\": \"31.134188\"" +
            "     }" +
            "   ]" +
            " }";

    static String JSON_INVALID_1 =
            " {\"matchedPoints\": " +
            "    {" +
            "       \"lat\": \"35.995063\"," +
            "       \"lon\": \"-78.908187\"" +
            "     };";


    static String JSON_INVALID_2 =
            " {\"matchedPoints\": [" +
            "    {" +
            "       \"lat\": 35.995063," +
            "       \"lon\": -78.908187" +
            "     }," +
            "   ]" +
            " }";

    static String JSON_INVALID_3 =
            " {\"matchedPoints\": [" +
            "    {" +
            "       \"lat\": \"abc\"," +
            "       \"lon\": \"-78.908187\"" +
            "     }" +
            "   ]" +
            " }";

    static String JSON_INVALID_4 =
            " {\"matchedPoints\": [" +
            "    {" +
            "       \"lon\": \"-78.908187\"" +
            "     }" +
            "   ]" +
            " }";
}

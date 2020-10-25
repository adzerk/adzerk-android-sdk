package com.adzerk.android.sdk.gson;

import android.location.Location;

import com.adzerk.android.sdk.BuildConfig;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MatchedPointsDeserializer implements JsonDeserializer<List<Location>> {

    String LAT_ATTRIBUTE = "lat";
    String LON_ATTRIBUTE = "lon";

    @Override
    public List<Location> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        List<Location> locations = new ArrayList<>();

        if (!json.isJsonArray()) {
            throw new JsonParseException("Unable to parse matchedPoints - array is expected");
        }

        JsonArray matchedPointsArray = json.getAsJsonArray();
        Iterator<JsonElement> jsonElementIterator = matchedPointsArray.iterator();
        while (jsonElementIterator.hasNext()) {
            JsonElement jsonElement = jsonElementIterator.next();
            if (!jsonElement.isJsonObject()) {
                throw new JsonParseException("Unable to parse matchedPoint - json object expected");
            }

            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Set<String> attributes = jsonObject.keySet();
            if (!attributes.contains(LAT_ATTRIBUTE) || !attributes.contains(LON_ATTRIBUTE)) {
                throw new JsonParseException("Unable to parse matchedPoint - 'lat' and 'lon' string values are expected");
            }

            String lat = jsonObject.get(LAT_ATTRIBUTE).getAsString();
            String lon = jsonObject.get(LON_ATTRIBUTE).getAsString();
            Location matchPoint = getLocation(lat, lon);
            locations.add(matchPoint);
        }

        return locations;
    }

    public Location getLocation(String lat, String lon) {
        Location location = new Location(BuildConfig.LIBRARY_PACKAGE_NAME);
        try {
            location.setLatitude(Double.parseDouble(lat));
            location.setLongitude(Double.parseDouble(lon));
        } catch (Exception e) {
            throw new JsonParseException("Unable to parse matchedPoint - 'lat' and 'lon' string values expected to be parsable as doubles");
        }
        return location;
    }
}

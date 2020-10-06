package com.adzerk.android.sdk.gson;

import com.adzerk.android.sdk.rest.Decision;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DecisionsDeserializer implements JsonDeserializer<Map<String, List<Decision>>> {

    @Override
    public Map<String, List<Decision>> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Map<String, List<Decision>> decisions = new HashMap<>();

        JsonObject winnersObject = json.getAsJsonObject();
        Set<String> placementNames = winnersObject.keySet();
        String firstPlacement = placementNames.iterator().next();

        if (winnersObject.get(firstPlacement) instanceof JsonArray) {
            // multi-winners response
            for (String placementName : placementNames) {
                List<Decision> decisionList = new ArrayList<>();
                JsonArray multiWinners = winnersObject.getAsJsonArray(placementName);

                for (JsonElement nextWinner : multiWinners) {
                    if (nextWinner instanceof JsonObject) {
                        JsonObject decisionObject = nextWinner.getAsJsonObject();
                        Decision decision = context.deserialize(decisionObject, Decision.class);
                        decisionList.add(decision);
                    }
                }
                decisions.put(placementName, decisionList);
            }
        } else {
            // single winner response
            for (String placementName : placementNames) {
                JsonElement member = winnersObject.get(placementName);
                if (member instanceof JsonObject) {
                    JsonObject decisionObject = winnersObject.getAsJsonObject(placementName);
                    Decision decision = context.deserialize(decisionObject, Decision.class);
                    List<Decision> decisionList = new ArrayList<>();
                    decisionList.add(decision);
                    decisions.put(placementName, decisionList);
                } else if (member instanceof JsonNull) {
                    decisions.put(placementName, null);
                } else {
                    throw new JsonParseException("Expected Object or null");
                }
            }
        }

        return decisions;
    }
}

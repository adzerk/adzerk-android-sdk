package com.adzerk.android.sdk.rest;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;

import java.util.HashMap;
import java.util.Map;

public class AdditionalOptions {

    Map<String, JsonElement> options;

    public JsonElement get(String key) {
        JsonElement element = options.get(key);
        return element == null ? JsonNull.INSTANCE : element;
    }

    public Map<String, JsonElement> getAll() {
        return options;
    }

    public static class Builder {
        private Map<String, JsonElement> options;
        private Gson gson;

        public Builder() {

        }

        /**
         * Add an additional option with a string value
         *
         * @param key   name of json attribute
         * @param value a String value
         * @return request builder
         */
        public Builder add(String key, String value) {
            addInternal(key, value == null ? JsonNull.INSTANCE : new JsonPrimitive(value));
            return this;
        }

        /**
         * Add an additional option with a numeric value
         *
         * @param key   name of json attribute
         * @param value a Number values
         * @return request builder
         */
        public Builder add(String key, Number value) {
            addInternal(key, value == null ? JsonNull.INSTANCE : new JsonPrimitive(value));
            return this;
        }

        /**
         * Add an additional option with a boolean value
         *
         * @param key   name of json attribute
         * @param value a Boolean values
         * @return request builder
         */
        public Builder add(String key, Boolean value) {
            addInternal(key, value == null ? JsonNull.INSTANCE : new JsonPrimitive(value));
            return this;
        }

        /**
         * Add an additional option with array of string values
         *
         * @param key    name of json attribute
         * @param values array of String values
         * @return request builder
         */
        public Builder add(String key, String[] values) {
            JsonArray jsonArray = new JsonArray();
            if (values != null) {
                for (String value : values) {
                    jsonArray.add(value);
                }
            }
            addInternal(key, jsonArray);
            return this;
        }

        /**
         * Add an additional option with array of numeric values
         *
         * @param key    name of json attribute
         * @param values array of Number values
         * @return request builder
         */
        public Builder add(String key, Number[] values) {
            JsonArray jsonArray = new JsonArray();
            if (values != null) {
                for (Number value : values) {
                    jsonArray.add(value);
                }
            }
            addInternal(key, jsonArray);
            return this;
        }

        /**
         * Add an additional option with array of boolean values
         *
         * @param key    name of json attribute
         * @param values array of boolean values
         * @return request builder
         */
        public Builder add(String key, Boolean[] values) {
            JsonArray jsonArray = new JsonArray();
            if (values != null) {
                for (Boolean value : values) {
                    jsonArray.add(value);
                }
            }
            addInternal(key, jsonArray);
            return this;
        }

        /**
         * Add an additional option with array of object values
         *
         * @param key    name of json attribute
         * @param values array of object values
         * @return request builder
         */
        public Builder add(String key, Object[] values) {
            if (gson == null) {
                gson = new Gson();
            }
            JsonArray jsonArray = new JsonArray();
            if (values != null) {
                for (Object value : values) {
                    JsonElement jsonElement = gson.toJsonTree(value);
                    jsonArray.add(jsonElement);
                }
            }
            addInternal(key, jsonArray);
            return this;
        }

        /**
         * Add an additional option where value is of JsonElement type.
         *
         * @param key   name of json attribute
         * @param value json element
         * @return request builder
         */
        public Builder add(String key, JsonElement value) {
            //options
            if (options == null) {
                options = new HashMap<>();
            }
            addInternal(key, value);
            return this;
        }

        /**
         * Add an additional option where value is an Object serializable to json.
         *
         * @param key   name of json attribute
         * @param value an Object that can be auto-serialized to json
         * @return request builder
         */
        public Builder add(String key, Object value) {
            if (gson == null) {
                gson = new Gson();
            }
            JsonElement jsonElement = gson.toJsonTree(value);
            addInternal(key, jsonElement);
            return this;
        }

        private void addInternal(String key, JsonElement jsonElement) {
            if (options == null) {
                options = new HashMap<>();
            }
            if (options.containsKey(key)) {
                throw new IllegalArgumentException("Options can not have the same key: " + key);
            }
            options.put(key, jsonElement == null ? JsonNull.INSTANCE : jsonElement);
        }

        /**
         * Create the AdditionalOptions object
         *
         * @return ad request
         * @throws IllegalStateException if the request has no placements.
         */
        public AdditionalOptions build() {
            return new AdditionalOptions(this);
        }
    }

    // end: AdditionalOptions.Builder

    private AdditionalOptions(AdditionalOptions.Builder builder) {
        if (builder.options != null) {
            this.options = builder.options;
        }
    }
}

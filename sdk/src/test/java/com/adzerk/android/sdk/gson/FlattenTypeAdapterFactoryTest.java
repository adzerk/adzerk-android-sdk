package com.adzerk.android.sdk.gson;

import com.adzerk.android.sdk.BuildConfig;
import com.adzerk.android.sdk.rest.AdditionalOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(RobolectricTestRunner.class)
@Config(sdk=25, constants= BuildConfig.class)
public class FlattenTypeAdapterFactoryTest {

    @FlattenAdditionalOptions(fieldName = "additionalOptions")
    public class AdditionalOptionsContainer {
        AdditionalOptions additionalOptions;

        public AdditionalOptionsContainer() {
            additionalOptions = new AdditionalOptions.Builder()
                    .add("int1", 1)
                    .add("string1", "string1Value")
                    .add("booleanFalse", false)
                    .build();
        }
    }

    @FlattenAdditionalOptions(fieldName = "additionalOptions")
    public class AdditionalOptionsInvalidContainer {
        Map<String, String> additionalOptions;

        public AdditionalOptionsInvalidContainer() {
            additionalOptions = new HashMap<>();
            additionalOptions.put("key", "value");
        }
    }

    @FlattenAdditionalOptions(fieldName = "additionalOptions")
    public class AdditionalOptionsInvalidContainer2 {
        AdditionalOptions otherOptions;

        public AdditionalOptionsInvalidContainer2() {
            otherOptions = new AdditionalOptions.Builder()
                    .add("string1", "string1Value")
                    .build();
        }
    }

    Gson gson;
    AdditionalOptionsContainer additionalOptionsContainer;

    @Before
    public void setUp() throws Exception {
        gson = new GsonBuilder().registerTypeAdapterFactory(new FlattenTypeAdapterFactory()).setPrettyPrinting().create();
        additionalOptionsContainer = new AdditionalOptionsContainer();
    }

    @Test
    public void ShouldSerializeJson_WithoutAdditionalOptionsAttribute() {
        String json = gson.toJson(additionalOptionsContainer);
        assertEquals(JSON, json);
        assertFalse(json.contains("additionalOptions"));
    }

    @Test(expected = JsonSyntaxException.class)
    public void ShouldThrowException_FieldTypeIsNotCorrectType() {
        gson.toJson(new AdditionalOptionsInvalidContainer());
    }

    @Test(expected = JsonSyntaxException.class)
    public void ShouldThrowException_FieldDoesNotExist() {
        gson.toJson(new AdditionalOptionsInvalidContainer2());
    }

    public String JSON = "{\n" +
            "  \"int1\": 1,\n" +
            "  \"string1\": \"string1Value\",\n" +
            "  \"booleanFalse\": false\n" +
            "}";
}

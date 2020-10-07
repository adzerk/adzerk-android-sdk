package com.adzerk.android.sdk.rest;

import com.adzerk.android.sdk.BuildConfig;
import com.adzerk.android.sdk.TestData.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(sdk=25, constants= BuildConfig.class)
public class AdditionalOptionsTest {

    String[] strings = new String[] {"value1","value2","value3" };
    Integer[] ints = new Integer[] {1, 2, 3 };
    Float[] floats = new Float[] {1.9f, 2.9f, 3.9f };
    Long[] longs = new Long[] {100L, 200L, 300L };
    Double[] doubles = new Double[] { 4.9d, 5.9d, 6.9d};
    Boolean[] booleans = new Boolean[] {true, true, false };

    ObjectWithDefaultFieldNames basicObject1 = new ObjectWithDefaultFieldNames("string1", 1, true);
    ObjectWithDefaultFieldNames basicObject2 = new ObjectWithDefaultFieldNames("string2", 2, true);
    ObjectWithDefaultFieldNames basicObject3 = new ObjectWithDefaultFieldNames("string3", 3, false);
    ObjectWithDefaultFieldNames[] basicObjects = new ObjectWithDefaultFieldNames[] {basicObject1, basicObject2, basicObject3};

    ObjectWithCustomFieldNames customObject1 = new ObjectWithCustomFieldNames("string1", 1, true);
    ObjectWithCustomFieldNames customObject2 = new ObjectWithCustomFieldNames("string2", 2, true);
    ObjectWithCustomFieldNames customObject3 = new ObjectWithCustomFieldNames("string3", 3, false);
    ObjectWithCustomFieldNames[] customObjects = new ObjectWithCustomFieldNames[] {customObject1, customObject2, customObject3};


    @Test(expected = IllegalArgumentException.class)
    public void itShouldThrowExceptionWhenKeyExists() {
        AdditionalOptions options = new AdditionalOptions.Builder()
                .add("key", "value1")
                .add("key", "value2")
                .build();
    }

    @Test
    public void itShouldBuildFromPrimitives() {
        try {
            AdditionalOptions options = new AdditionalOptions.Builder()
                    .add("string", "value1")
                    .add("int", 123)
                    .add("float", 4.56f)
                    .add("long", 789L)
                    .add("double", 7.89d)
                    .add("boolean", true)
                    .build();

            JsonElement stringElement = options.get("string");
            assertNotNull(stringElement);
            assertThat(stringElement.isJsonPrimitive());
            assertEquals("value1", stringElement.getAsString());

            JsonElement intElement = options.get("int");
            assertNotNull(intElement);
            assertThat(intElement.isJsonPrimitive());
            assertEquals(123, intElement.getAsInt());

            JsonElement floatElement = options.get("float");
            assertNotNull(floatElement);
            assertThat(floatElement.isJsonPrimitive());
            assertEquals(4.56f, floatElement.getAsFloat(), 0.001);

            JsonElement longElement = options.get("long");
            assertNotNull(longElement);
            assertThat(longElement.isJsonPrimitive());
            assertEquals(789L, longElement.getAsLong());

            JsonElement doubleElement = options.get("double");
            assertNotNull(doubleElement);
            assertThat(doubleElement.isJsonPrimitive());
            assertEquals(7.89d, doubleElement.getAsDouble(), 0.001);

            JsonElement booleanElement = options.get("boolean");
            assertNotNull(booleanElement);
            assertThat(booleanElement.isJsonPrimitive());
            assertEquals(true, booleanElement.getAsBoolean());
        }  catch (IllegalArgumentException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void itShouldBuildFromArrays() {
        try {
            AdditionalOptions options = new AdditionalOptions.Builder()
                    .add("strings", strings)
                    .add("ints", ints)
                    .add("floats", floats)
                    .add("longs", longs)
                    .add("doubles", doubles)
                    .add("booleans", booleans)
                    .build();

            JsonElement stringsElement = options.get("strings");
            assertNotNull(stringsElement);
            assertThat(stringsElement.isJsonArray());
            JsonArray stringsArray = stringsElement.getAsJsonArray();
            assertEquals(strings[0], stringsArray.get(0).getAsString());
            assertEquals(strings[1], stringsArray.get(1).getAsString());
            assertEquals(strings[2], stringsArray.get(2).getAsString());

            JsonElement intsElement = options.get("ints");
            assertNotNull(intsElement);
            assertThat(intsElement.isJsonArray());
            JsonArray intsArray = intsElement.getAsJsonArray();
            assertEquals((int)ints[0], intsArray.get(0).getAsInt());
            assertEquals((int)ints[1], intsArray.get(1).getAsInt());
            assertEquals((int)ints[2], intsArray.get(2).getAsInt());

            JsonElement floatsElement = options.get("floats");
            assertThat(floatsElement.isJsonArray());
            JsonArray floatsArray = floatsElement.getAsJsonArray();
            assertEquals((float)floats[0], floatsArray.get(0).getAsFloat(), 0.001);
            assertEquals((float)floats[1], floatsArray.get(1).getAsFloat(), 0.001);
            assertEquals((float)floats[2], floatsArray.get(2).getAsFloat(), 0.001);

            JsonElement longsElement = options.get("longs");
            assertNotNull(longsElement);
            assertThat(longsElement.isJsonArray());
            JsonArray longsArray = longsElement.getAsJsonArray();
            assertEquals((long)longs[0], longsArray.get(0).getAsLong());
            assertEquals((long)longs[1], longsArray.get(1).getAsLong());
            assertEquals((long)longs[2], longsArray.get(2).getAsLong());

            JsonElement doublesElement = options.get("doubles");
            assertThat(doublesElement.isJsonArray());
            JsonArray doublesArray = doublesElement.getAsJsonArray();
            assertEquals((double)doubles[0], doublesArray.get(0).getAsDouble(), 0.001);
            assertEquals((double)doubles[1], doublesArray.get(1).getAsDouble(), 0.001);
            assertEquals((double)doubles[2], doublesArray.get(2).getAsDouble(), 0.001);

            JsonElement booleansElement = options.get("booleans");
            assertThat(booleansElement.isJsonArray());
            JsonArray booleansArray = booleansElement.getAsJsonArray();
            assertEquals((boolean)booleans[0], booleansArray.get(0).getAsBoolean());
            assertEquals((boolean)booleans[1], booleansArray.get(1).getAsBoolean());
            assertEquals((boolean)booleans[2], booleansArray.get(2).getAsBoolean());
        }  catch (IllegalArgumentException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void itShouldAllowNullValues() {
        try {
            AdditionalOptions options = new AdditionalOptions.Builder()
                    .add("string", (String) null)
                    .add("int", (Integer) null)
                    .add("float", (Float) null)
                    .add("long", (Long) null)
                    .add("double", (Double) null)
                    .add("boolean", (Boolean) null)
                    .add("jsonElem", (JsonElement) null)
                    .add("javaObject", (Object) null)
                    .add("stringArray", (String[]) null)
                    .build();

            JsonElement stringElement = options.get("string");
            assertNotNull(stringElement);
            assertThat(stringElement.isJsonNull());

            JsonElement intElement = options.get("int");
            assertNotNull(intElement);
            assertThat(intElement.isJsonNull());

            JsonElement floatElement = options.get("float");
            assertNotNull(floatElement);
            assertThat(floatElement.isJsonNull());

            JsonElement longElement = options.get("long");
            assertNotNull(longElement);
            assertThat(longElement.isJsonNull());

            JsonElement doubleElement = options.get("double");
            assertNotNull(doubleElement);
            assertThat(doubleElement.isJsonNull());

            JsonElement booleanElement = options.get("boolean");
            assertNotNull(booleanElement);
            assertThat(booleanElement.isJsonNull());

            JsonElement jsonElem = options.get("jsonElem");
            assertNotNull(jsonElem);
            assertThat(jsonElem.isJsonNull());

            JsonElement objectElement = options.get("javaObject");
            assertNotNull(objectElement);
            assertThat(objectElement.isJsonNull());

            JsonElement stringArrayElement = options.get("stringArray");
            assertNotNull(stringArrayElement);
            assertThat(stringArrayElement.isJsonNull());
            assertThat(stringArrayElement.isJsonArray());
            assertEquals(0, stringArrayElement.getAsJsonArray().size());
            assertEquals("[]", new Gson().toJson(stringArrayElement));
        }  catch (IllegalArgumentException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void itShouldBuildFromJsonElements() {
        try {
            JsonPrimitive jsonPrimitive = new JsonPrimitive(true);

            JsonArray stringArray = new JsonArray();
            for (String next : strings) {
                stringArray.add(next);
            }

            String JSON_DATA = "{\"outerObject\":{\"stringKey\":\"string1\"},\n" +
                    "  \"innerObject\":[{\n" +
                    "  \"booleanKey\":true,\n" +
                    "  \"intKey\":99,\n" +
                    "  \"intArray\":[1,2,3,4]}]}";

            JsonElement jsonElementFromString = new JsonParser().parse(JSON_DATA);

            AdditionalOptions options = new AdditionalOptions.Builder()
                    .add("primitiveBoolean", jsonPrimitive)
                    .add("stringArray", stringArray)
                    .add("jsonElement", jsonElementFromString)
                    .build();

            JsonElement booleanElement = options.get("primitiveBoolean");
            assertNotNull(booleanElement);
            assertThat(booleanElement.isJsonPrimitive());
            assertEquals(true, booleanElement.getAsBoolean());

            JsonElement stringsElement = options.get("stringArray");
            assertNotNull(stringsElement);
            assertThat(stringsElement.isJsonArray());
            JsonArray stringsArray = stringsElement.getAsJsonArray();
            assertEquals(strings[0], stringsArray.get(0).getAsString());
            assertEquals(strings[1], stringsArray.get(1).getAsString());
            assertEquals(strings[2], stringsArray.get(2).getAsString());

            JsonElement jsonElement = options.get("jsonElement");
            assertEquals(JSON_DATA.replaceAll("\\s", ""), jsonElement.toString());


        } catch (IllegalArgumentException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void itShouldBuildFromObjects() {

        try {
            AdditionalOptions options = new AdditionalOptions.Builder()
                    .add("myBasicOption", basicObject1)
                    .add("myCustomOption", customObject1)
                    .build();

            JsonElement myNewOption = options.get("myBasicOption");
            assertNotNull(myNewOption);
            assertThat(myNewOption.isJsonObject());
            JsonObject myNewOptionAsJsonObject = myNewOption.getAsJsonObject();
            assertNotNull(myNewOptionAsJsonObject.get("stringField"));
            assertEquals("string1", myNewOptionAsJsonObject.get("stringField").getAsString());
            assertNotNull(myNewOptionAsJsonObject.get("intField"));
            assertEquals(1, myNewOptionAsJsonObject.get("intField").getAsInt());
            assertNotNull(myNewOptionAsJsonObject.get("booleanField"));
            assertEquals(true, myNewOptionAsJsonObject.get("booleanField").getAsBoolean());

            JsonElement myCustomOption = options.get("myCustomOption");
            assertNotNull(myCustomOption);
            assertThat(myCustomOption.isJsonObject());
            JsonObject myCustomOptionAsJsonObject = myCustomOption.getAsJsonObject();
            assertNotNull(myCustomOptionAsJsonObject.get("name"));
            assertEquals("string1", myCustomOptionAsJsonObject.get("name").getAsString());
            assertNotNull(myCustomOptionAsJsonObject.get("age"));
            assertEquals(1, myCustomOptionAsJsonObject.get("age").getAsInt());
            assertNotNull(myCustomOptionAsJsonObject.get("employee"));
            assertEquals(true, myCustomOptionAsJsonObject.get("employee").getAsBoolean());

        } catch (IllegalArgumentException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void itShouldBuildFromObjectArrays() {
        try {
            AdditionalOptions options = new AdditionalOptions.Builder()
                    .add("myBasicObjectArray", basicObjects)
                    .add("myCustomObjectArray", customObjects)
                    .build();

            JsonElement basicObjectArrayElem = options.get("myBasicObjectArray");
            assertNotNull(basicObjectArrayElem);
            assertThat(basicObjectArrayElem.isJsonArray());
            JsonArray basicObjectArray = basicObjectArrayElem.getAsJsonArray();
            assertEquals(toJsonString(basicObjects[0]), basicObjectArray.get(0).getAsJsonObject().toString());
            assertEquals(toJsonString(basicObjects[1]), basicObjectArray.get(1).getAsJsonObject().toString());
            assertEquals(toJsonString(basicObjects[2]), basicObjectArray.get(2).getAsJsonObject().toString());

            JsonElement customObjectArrayElem = options.get("myCustomObjectArray");
            assertNotNull(customObjectArrayElem);
            assertThat(customObjectArrayElem.isJsonArray());
            JsonArray customObjectArray = customObjectArrayElem.getAsJsonArray();
            assertEquals(toJsonString(customObjects[0]), customObjectArray.get(0).getAsJsonObject().toString());
            assertEquals(toJsonString(customObjects[1]), customObjectArray.get(1).getAsJsonObject().toString());
            assertEquals(toJsonString(customObjects[2]), customObjectArray.get(2).getAsJsonObject().toString());
        }  catch (IllegalArgumentException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    private String toJsonString(Object o) {
        return new Gson().toJson(o);
    }
}

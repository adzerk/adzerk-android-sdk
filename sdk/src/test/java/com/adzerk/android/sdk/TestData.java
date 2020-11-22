package com.adzerk.android.sdk;

import com.google.gson.annotations.SerializedName;

public class TestData {

    public static class ObjectWithDefaultFieldNames {
        String stringField;
        int intField;
        boolean booleanField;

        public ObjectWithDefaultFieldNames(String s, int i, boolean b) {
            stringField = s;
            intField = i;
            booleanField = b;
        }
    }

    public static class ObjectWithCustomFieldNames {
        @SerializedName(value = "name")
        String stringField;
        @SerializedName(value = "age")
        int intField;
        @SerializedName(value = "employee")
        boolean booleanField;

        public ObjectWithCustomFieldNames(String s, int i, boolean b) {
            stringField = s;
            intField = i;
            booleanField = b;
        }
    }
}

package com.adzerk.android.sdk.gson;

import com.adzerk.android.sdk.rest.AdditionalOptions;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

public class FlattenTypeAdapterFactory implements TypeAdapterFactory {

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {

        final TypeAdapter<T> delegateAdapter = gson.getDelegateAdapter(this, type);

        TypeAdapter<T> result = new TypeAdapter<T>() {

            @Override
            public void write(JsonWriter out, T value) throws IOException {
                // use delegate for initial serialization
                JsonElement res = delegateAdapter.toJsonTree(value);

                // check for annotation - remove specified wrapper field and re-add additional options directly to root
                if (res.isJsonObject() && value.getClass().isAnnotationPresent(FlattenAdditionalOptions.class)) {
                    JsonObject jsonObject = res.getAsJsonObject();

                    FlattenAdditionalOptions annotation = value.getClass().getAnnotation(FlattenAdditionalOptions.class);
                    String fieldName = annotation.fieldName();
                    if (fieldName == null) {
                        throw new JsonSyntaxException("FlattenAdditionalOptions annotation must specify a 'fieldName'");
                    }

                    try {
                        Field additionalOptionsField = value.getClass().getDeclaredField(fieldName);
                        if (additionalOptionsField.getType() != AdditionalOptions.class) {
                            throw new JsonSyntaxException("Field '" + fieldName + "' is expected to have type AdditionalOptions" );
                        }

                        // remove default serialized object containing additional options by named field
                        jsonObject.remove(fieldName);

                        // now append any additional options directly to root of the object
                        AdditionalOptions additionalOptions = (AdditionalOptions) additionalOptionsField.get(value);
                        if (additionalOptions != null && additionalOptions.getAll() != null && !additionalOptions.getAll().isEmpty()) {
                            for (Map.Entry<String, JsonElement> option : additionalOptions.getAll().entrySet()) {
                                jsonObject.add(option.getKey(), option.getValue());
                            }
                        }
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                       throw new JsonSyntaxException(e);
                    }
                }

                gson.toJson(res, out);
            }

            @Override
            public T read(JsonReader in) throws IOException {
                return delegateAdapter.read(in);
            }

        }.nullSafe();

        return result;
    }
}

package com.example.catalogoanimales.api;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.example.catalogoanimales.model.Animal;
import com.example.catalogoanimales.model.AnimalImpl;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ApiClient {
    private static final String ANIMALS_FILE = "animales.json";
    private static Context context;
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Animal.class, new com.google.gson.JsonDeserializer<Animal>() {
                @Override
                public Animal deserialize(com.google.gson.JsonElement json, java.lang.reflect.Type typeOfT, com.google.gson.JsonDeserializationContext context) throws com.google.gson.JsonParseException {
                    return gson.fromJson(json, AnimalImpl.class);
                }
            })
            .create();

    public static void init(Context appContext) {
        context = appContext.getApplicationContext();
    }

    public static AnimalesResponse getAnimales() throws IOException {
        String jsonString = loadJSONFromAsset();
        return gson.fromJson(jsonString, AnimalesResponse.class);
    }

    private static String loadJSONFromAsset() throws IOException {
        if (context == null) {
            throw new IllegalStateException("ApiClient must be initialized with init(Context) first");
        }
        
        String json;
        try (InputStream is = context.getAssets().open(ANIMALS_FILE)) {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            json = new String(buffer, StandardCharsets.UTF_8);
        }
        return json;
    }
} 
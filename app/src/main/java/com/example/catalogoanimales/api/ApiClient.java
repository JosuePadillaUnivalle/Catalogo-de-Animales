package com.example.catalogoanimales.api;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.example.catalogoanimales.model.Animal;
import com.example.catalogoanimales.model.AnimalImpl;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://raw.githubusercontent.com/JosuePadillaUnivalle/Catalogo-de-Animales/main/";
    private static ApiService apiService;
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Animal.class, new com.google.gson.JsonDeserializer<Animal>() {
                @Override
                public Animal deserialize(com.google.gson.JsonElement json, java.lang.reflect.Type typeOfT, com.google.gson.JsonDeserializationContext context) throws com.google.gson.JsonParseException {
                    return gson.fromJson(json, AnimalImpl.class);
                }
            })
            .create();

    public static void init(Context appContext) {
        if (apiService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            apiService = retrofit.create(ApiService.class);
        }
    }

    public static ApiService getApiService() {
        if (apiService == null) {
            throw new IllegalStateException("ApiClient must be initialized with init(Context) first");
        }
        return apiService;
    }
} 
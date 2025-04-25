package com.example.catalogoanimales.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("api/animales.json")
    Call<AnimalesResponse> getAnimales();
} 
package com.example.deneme.interfaces;

import com.example.deneme.models.TokenResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface LogEasyApi {
    @GET ("oauth/direction")
    Call<TokenResponse> getDirection();

}

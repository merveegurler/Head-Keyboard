package com.example.deneme.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenResponse {
    @SerializedName("direction")
    @Expose
    private String direction;

    public String getDirection() {
        return direction;
    }
}

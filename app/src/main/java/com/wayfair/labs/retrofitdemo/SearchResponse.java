package com.wayfair.labs.retrofitdemo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResponse {

    @SerializedName("total_count")
    int total = 0;

    @SerializedName("items")
    List<Repo> items;
}

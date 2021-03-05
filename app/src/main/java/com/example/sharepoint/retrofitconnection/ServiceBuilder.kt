package com.example.sharepoint.retrofitconnection

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {

    fun makeRetrofitMaps() : ConnectionEndPoint{

        return Retrofit.Builder().baseUrl("https://maps.googleapis.com/maps/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ConnectionEndPoint::class.java)

    }
}
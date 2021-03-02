package com.example.sharepoint.retrofitconnection

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {

    fun makeRetrofitMaps() : ConnectionEndPoint{

        return Retrofit.Builder().baseUrl("")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ConnectionEndPoint::class.java)

    }
}
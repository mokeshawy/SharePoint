package com.example.sharepoint.retrofitconnection

import com.example.sharepoint.mapsfragment.MapsModel

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ConnectionEndPoint {

    @GET("api/directions/json")
   suspend fun direction(  @Query("origin") origin : String ,
                            @Query("destination") destination : String ,
                            @Query("key") key : String) : Response<MapsModel>
}
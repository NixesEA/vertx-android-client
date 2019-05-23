package com.nixesea.testvkr.network

import com.nixesea.testvkr.RespObj
import retrofit2.Call
import retrofit2.http.*


interface APIRequest {

    @GET("check")
    fun checkConnection(): Call<RespObj>

    @GET("jsonAnton")
    fun getJs(): Call<RespObj>

    @POST("saveData")
    fun saveSimulateData(@Body post:SimulatedData): Call<RespObj>

    @POST("myOwl/sparql")
    @FormUrlEncoded
    fun savePost(@Field("title") title:String): Call<SimulatedData>

}
package com.nixesea.testvkr.network

object APIBase {

    var BASE_URL = "http://192.168.100.4:1080/"

    var apiRequest: APIRequest? = null
        get() = RetrofitClient.getClient(BASE_URL)!!.create<APIRequest>(
            APIRequest::class.java)

}
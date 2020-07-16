package com.bugrui.jetpackproject.http

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("/xxxx")
    fun getOSSUploadData(@Body params: HashMap<String, Any?>): Call<ApiResponse<String?>>

}
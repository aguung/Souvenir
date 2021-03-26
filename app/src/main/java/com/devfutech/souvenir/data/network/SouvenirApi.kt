package com.devfutech.souvenir.data.network

import com.devfutech.souvenir.data.network.response.SouvenirResponse
import retrofit2.http.GET

interface SouvenirApi {
    @GET("exec?action=readAll&sheet=Souvenir")
    suspend fun getSouvenir(): SouvenirResponse
}
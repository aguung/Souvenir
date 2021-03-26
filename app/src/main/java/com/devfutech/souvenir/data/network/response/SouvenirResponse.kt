package com.devfutech.souvenir.data.network.response


import com.devfutech.souvenir.data.local.entity.Souvenir
import com.google.gson.annotations.SerializedName

data class SouvenirResponse(
    @SerializedName("records")
    val souvenirs: List<Souvenir> = arrayListOf()
)
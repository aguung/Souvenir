package com.devfutech.souvenir.data.local.entity


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "souvenir_table")
@Parcelize
data class Souvenir(
    @SerializedName("souvenirAddress")
    val souvenirAddress: String?,
    @SerializedName("souvenirCategory")
    val souvenirCategory: String?,
    @PrimaryKey
    @SerializedName("souvenirCode")
    val souvenirCode: String,
    @SerializedName("souvenirDescription")
    val souvenirDescription: String?,
    @SerializedName("souvenirImageUrl")
    val souvenirImageUrl: String?,
    @SerializedName("souvenirLatLong")
    val souvenirLatLong: String?,
    @SerializedName("souvenirName")
    val souvenirName: String?,
    @SerializedName("souvenirOpenClosed")
    val souvenirOpenClosed: String?,
    @SerializedName("souvenirTelp")
    val souvenirTelp: String?,
) : Parcelable
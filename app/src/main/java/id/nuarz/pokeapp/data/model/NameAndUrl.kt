package id.nuarz.pokeapp.data.model

import com.google.gson.annotations.SerializedName

data class NameAndUrl(
    @SerializedName("name")
    val name: String,

    @SerializedName("url")
    val url: String
)

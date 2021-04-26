package id.nuarz.pokeapp.data.model

import com.google.gson.annotations.SerializedName

data class Names(
    @SerializedName("language")
    val language: NameAndUrl,

    @SerializedName("name")
    val name: String
)

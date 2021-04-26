package id.nuarz.pokeapp.data.model

import com.google.gson.annotations.SerializedName

data class FlavorText(
    @SerializedName("flavor_text")
    val flavorText : String,

    @SerializedName("language")
    val language : NameAndUrl
)

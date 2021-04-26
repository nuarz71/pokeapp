package id.nuarz.pokeapp.data.model

import com.google.gson.annotations.SerializedName

data class Sprite(
    @SerializedName("front_default")
    val frontDefault: String?,
    @SerializedName("front_shiny")
    val frontShiny: String?
)

package id.nuarz.pokeapp.data.model

import com.google.gson.annotations.SerializedName

data class PokemonType(
    @SerializedName("slot")
    val slot: Int,

    @SerializedName("type")
    val type: NameAndUrl
)

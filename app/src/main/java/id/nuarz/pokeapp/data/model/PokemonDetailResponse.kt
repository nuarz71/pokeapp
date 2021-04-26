package id.nuarz.pokeapp.data.model

import com.google.gson.annotations.SerializedName

data class PokemonDetailResponse(

    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("abilities")
    val abilities: List<PokemonAbility>,

    @SerializedName("stats")
    val stat: List<PokemonStat>,

    @SerializedName("types")
    val types: List<PokemonType>?,

    @SerializedName("sprites")
    val sprites: Sprite
)

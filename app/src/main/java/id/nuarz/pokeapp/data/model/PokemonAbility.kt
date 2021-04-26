package id.nuarz.pokeapp.data.model

import com.google.gson.annotations.SerializedName

data class PokemonAbility(
    @SerializedName("ability")
    val ability: NameAndUrl,

    @SerializedName("is_hidden")
    val isHidden: Boolean,

    @SerializedName("slot")
    val slot: Int
)

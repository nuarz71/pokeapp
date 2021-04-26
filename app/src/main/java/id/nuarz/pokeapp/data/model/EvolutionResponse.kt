package id.nuarz.pokeapp.data.model

import com.google.gson.annotations.SerializedName

data class EvolutionResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("chain")
    val chain: Evolve
)

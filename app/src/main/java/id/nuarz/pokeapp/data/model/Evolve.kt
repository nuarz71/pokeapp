package id.nuarz.pokeapp.data.model

import com.google.gson.annotations.SerializedName

data class Evolve(
    @SerializedName("evolution_details")
    val evolutionDetails: List<EvolutionDetails>,

    @SerializedName("evolves_to")
    val evolvesTo: List<Evolve>,

    @SerializedName("species")
    val species: NameAndUrl
)

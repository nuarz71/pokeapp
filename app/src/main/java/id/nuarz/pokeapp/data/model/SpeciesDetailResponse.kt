package id.nuarz.pokeapp.data.model

import com.google.gson.annotations.SerializedName

data class SpeciesDetailResponse(

    @SerializedName("capture_rate")
    val captureRate: Int,

    @SerializedName("egg_groups")
    val eggGroups: List<NameAndUrl>,

    @SerializedName("gender_rate")
    val genderRate: Int,

    @SerializedName("has_gander_differences")
    val hasGenderDifferences: Boolean,

    @SerializedName("id")
    val id: Int,

    @SerializedName("generation")
    val generation: NameAndUrl,

    @SerializedName("habitat")
    val habitat: NameAndUrl,

    @SerializedName("evolution_chain")
    val evolutionChain: EvolutionChain,

    @SerializedName("flavor_text_entries")
    val flavorTextEntries: List<FlavorText>,

    @SerializedName("hatch_counter")
    val hatchCounter: Int
)
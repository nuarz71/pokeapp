package id.nuarz.pokeapp.data.model

import com.google.gson.annotations.SerializedName

data class EvolutionDetails(
    @SerializedName("item")
    val item: NameAndUrl?,

    @SerializedName("min_affection")
    val minAffection: Int?,

    @SerializedName("min_beauty")
    val minBeauty: Int?,

    @SerializedName("min_happiness")
    val minHappiness: Int?,

    @SerializedName("min_level")
    val minLevel: Int?,

    @SerializedName("trigger")
    val trigger: NameAndUrl
)

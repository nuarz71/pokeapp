package id.nuarz.pokeapp.data.model

import com.google.gson.annotations.SerializedName

data class AbilityResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("flavor_text_entries")
    val flavorTextEntries: List<FlavorText>,

    @SerializedName("name")
    val name: String,

    @SerializedName("names")
    val names: List<Names>
)

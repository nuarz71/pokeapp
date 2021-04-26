package id.nuarz.pokeapp.data.model

import com.google.gson.annotations.SerializedName

data class DetailNamesResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("names")
    val names: List<Names>
)

package id.nuarz.pokeapp.data.model

import com.google.gson.annotations.SerializedName


data class ListResponse<out T>(
    @SerializedName("count")
    val count: Int,

    @SerializedName("next")
    val next: String?,

    @SerializedName("previous")
    val previous: String?,

    @SerializedName("results")
    val result: List<T>
)

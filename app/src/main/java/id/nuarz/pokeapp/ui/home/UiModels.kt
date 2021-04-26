package id.nuarz.pokeapp.ui.home

import android.os.Parcelable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

interface HomeUiModel

@Keep
@Parcelize
data class PokemonItemModel(
    val id: Int,
    val name: String?,
    val number: String,
    val imageUrl: String?,
    val elements: List<ElementItemModel>
) : HomeUiModel, Parcelable

@Keep
@Parcelize
data class ElementItemModel(
    val name: String?,

    @DrawableRes
    val iconResId: Int,
    @ColorRes
    val colorResId: Int
) : Parcelable
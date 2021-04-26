package id.nuarz.pokeapp.ui.detail

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import id.nuarz.pokeapp.ui.home.HomeUiModel

interface DetailUiModel

data class StatItemModel(
    val hp: Int,
    val atk: Int,
    val def: Int,
    val satk: Int,
    val sdef: Int,
    val spd: Int
) : DetailUiModel

data class WeaknessItemModel(
    val title: String,
    val items: List<ElementWeaknessItemModel>
) : DetailUiModel

data class ElementWeaknessItemModel(
    val name: String?,
    @DrawableRes
    val iconResId: Int,
    @ColorRes
    val colorResId: Int,
    val effect: String?
)

data class AbilitiesItemModel(
    val items: List<AbilityItemModel>
) : DetailUiModel

data class AbilityItemModel(
    val title: String?,
    val overview: String?
)

data class BreedingItemModel(
    val eggGroups: String?,
    val hatchTime: Int,
    val genderRate: Int
) : DetailUiModel

data class CaptureItemModel(
    val habitats: String?,
    val generation: String?,
    val captureRate: Int
) : DetailUiModel

data class SpritesItemModel(
    val normalImageUrl: String?,
    val shinyImageUrl: String?
) : DetailUiModel

data class EvolutionItemModel(
    val fromImageUrl: String,
    val fromName: String,
    val toImageUrl: String,
    val toName: String,
    val trigger: String
) : DetailUiModel

object LoadingItemModel : DetailUiModel, HomeUiModel
object ConnectionError : DetailUiModel, HomeUiModel
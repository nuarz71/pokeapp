package id.nuarz.pokeapp.ui.detail.adapter

import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import id.nuarz.pokeapp.R
import id.nuarz.pokeapp.core.BaseViewHolder
import id.nuarz.pokeapp.databinding.ListItemBreedingCaptureBinding
import id.nuarz.pokeapp.ui.detail.BreedingItemModel
import kotlin.math.roundToInt

class BreedingViewHolder(
    private val binding: ListItemBreedingCaptureBinding,
    @ColorInt
    val color: Int
) : BaseViewHolder<BreedingItemModel>(binding.root) {

    init {
        binding.tvTitle.setTextColor(color)
        binding.tvTitle.setText(R.string.item_title_breeding)

        binding.tvSubtitle1.setTextColor(color)
        binding.tvSubtitle1.setText(R.string.subtitle_breeding_eeg_group)

        binding.tvSubtitle2.setTextColor(color)
        binding.tvSubtitle2.setText(R.string.subtitle_breeding_hatch_time)

        binding.tvSubtitle3.setTextColor(color)
        binding.tvSubtitle3.setText(R.string.subtitle_breeding_gender)

        binding.ivProgressIcon.visibility = View.VISIBLE
    }

    override fun bind(model: BreedingItemModel) {
        binding.tvOverview1.text = model.eggGroups

        val step = 255 * (model.hatchTime + 1)
        val hatchOverview = itemView.context.getString(
            R.string.overview_hatch_time, step, model.hatchTime
        )
        binding.tvOverview2.text = hatchOverview

        when (model.genderRate) {
            -1 -> {
                val blue = ContextCompat.getColor(itemView.context, R.color.color_type_dragon)
                binding.tvOverview3.setTextColor(blue)
                binding.tvOverview3.text = "0%"
            }
            else -> {
                val femalePercent = (model.genderRate / 8F) * 100F
                val malePercent = 100F - femalePercent
                val overview = itemView.context.getString(
                    R.string.overview_gender,
                    String.format("%.1f", malePercent) + "%",
                    String.format("%.1f", femalePercent) + "%"
                )
                binding.tvOverview3.text = HtmlCompat.fromHtml(
                    overview,
                    HtmlCompat.FROM_HTML_MODE_COMPACT
                )

                binding.progress3.progress = malePercent.roundToInt()
            }
        }
    }
}
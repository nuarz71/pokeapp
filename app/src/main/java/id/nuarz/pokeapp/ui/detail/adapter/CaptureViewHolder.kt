package id.nuarz.pokeapp.ui.detail.adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import id.nuarz.pokeapp.R
import id.nuarz.pokeapp.core.BaseViewHolder
import id.nuarz.pokeapp.databinding.ListItemBreedingCaptureBinding
import id.nuarz.pokeapp.ui.detail.CaptureItemModel
import kotlin.math.roundToInt

class CaptureViewHolder(
    private val binding: ListItemBreedingCaptureBinding,
    @ColorInt
    val color: Int
) : BaseViewHolder<CaptureItemModel>(binding.root) {

    init {
        binding.tvTitle.setTextColor(color)
        binding.tvTitle.setText(R.string.content_title_capture)

        binding.tvSubtitle1.setTextColor(color)
        binding.tvSubtitle1.setText(R.string.subtitle_capture_habitat)

        binding.tvSubtitle2.setTextColor(color)
        binding.tvSubtitle2.setText(R.string.subtitle_capture_generation)

        binding.tvSubtitle3.setTextColor(color)
        binding.tvSubtitle3.setText(R.string.subtitle_capture_rate)

        val blue = ContextCompat.getColor(itemView.context, R.color.color_type_dragon)
        binding.tvOverview3.setTextColor(blue)

        binding.ivProgressIcon.setImageResource(R.drawable.ic_pokeball)
        binding.ivProgressIcon.imageTintList = ColorStateList.valueOf(blue)

        binding.progress3.setIndicatorColor(blue)
        binding.progress3.trackColor = ContextCompat.getColor(
            itemView.context,
            R.color.color_progress_track
        )
    }

    @SuppressLint("SetTextI18n")
    override fun bind(model: CaptureItemModel) {
        binding.tvOverview1.text = model.habitats
        binding.tvOverview2.text = model.generation

        val percent = (model.captureRate / 255F) * 100F
        val progress = percent.roundToInt()
        binding.tvOverview3.text = String.format("%.1f", percent) + "%"
        binding.progress3.progress = progress
    }
}
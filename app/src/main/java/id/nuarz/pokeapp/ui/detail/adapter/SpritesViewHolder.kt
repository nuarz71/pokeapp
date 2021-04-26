package id.nuarz.pokeapp.ui.detail.adapter

import android.graphics.drawable.GradientDrawable
import androidx.annotation.ColorInt
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import id.nuarz.pokeapp.core.BaseViewHolder
import id.nuarz.pokeapp.databinding.ListItemSpritesBinding
import id.nuarz.pokeapp.ui.detail.SpritesItemModel

class SpritesViewHolder(
    private val binding: ListItemSpritesBinding,
    @ColorInt
    val color: Int,
    @ColorInt
    val lightColor: Int
) : BaseViewHolder<SpritesItemModel>(binding.root) {

    init {
        (binding.constrainLayout.background as? GradientDrawable)?.also {
            it.colors = intArrayOf(lightColor, color)
        }

        binding.tvTitle.setTextColor(color)
        binding.tvNormal.setTextColor(color)
        binding.tvShiny.setTextColor(color)
    }

    override fun bind(model: SpritesItemModel) {
        if (model.normalImageUrl != null) {
            Glide.with(binding.ivNormal)
                .load(model.normalImageUrl)
                .thumbnail(0.5f)
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(binding.ivNormal)
        }

        if (model.shinyImageUrl != null) {
            Glide.with(binding.ivShiny)
                .load(model.shinyImageUrl)
                .thumbnail(0.5f)
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(binding.ivShiny)
        }
    }
}
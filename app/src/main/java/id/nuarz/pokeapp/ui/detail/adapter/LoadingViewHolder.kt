package id.nuarz.pokeapp.ui.detail.adapter

import androidx.annotation.ColorInt
import id.nuarz.pokeapp.core.BaseViewHolder
import id.nuarz.pokeapp.databinding.ListItemLoadingBinding
import id.nuarz.pokeapp.ui.detail.LoadingItemModel

class LoadingViewHolder(
    binding: ListItemLoadingBinding,
    @ColorInt color: Int
) :
    BaseViewHolder<LoadingItemModel>(binding.root) {
    init {
        binding.progressBar.setIndicatorColor(color)
    }

    override fun bind(model: LoadingItemModel) {

    }
}
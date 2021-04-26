package id.nuarz.pokeapp.ui.detail.adapter

import androidx.annotation.ColorInt
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import id.nuarz.pokeapp.core.BaseViewHolder
import id.nuarz.pokeapp.databinding.ListItemEvolutionBinding
import id.nuarz.pokeapp.ui.detail.EvolutionItemModel

class EvolutionViewHolder(
    private val binding: ListItemEvolutionBinding,
    @ColorInt
    val color: Int
) : BaseViewHolder<EvolutionItemModel>(binding.root) {

    init {
        binding.tvTrigger.setTextColor(color)
    }

    override fun bind(model: EvolutionItemModel) {

        binding.tvNameFrom.text = model.fromName
        Glide.with(binding.ivFrom)
            .load(model.fromImageUrl)
            .thumbnail(0.5f)
            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
            .into(binding.ivFrom)

        binding.tvNameTo.text = model.toName
        Glide.with(binding.ivTo)
            .load(model.toImageUrl)
            .thumbnail(0.5f)
            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
            .into(binding.ivTo)

        binding.tvTrigger.text = model.trigger
    }
}
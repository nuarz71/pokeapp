package id.nuarz.pokeapp.ui.detail.adapter

import androidx.annotation.ColorInt
import androidx.recyclerview.widget.GridLayoutManager
import id.nuarz.pokeapp.core.BaseViewHolder
import id.nuarz.pokeapp.databinding.ListItemWeaknessesBinding
import id.nuarz.pokeapp.ui.detail.WeaknessItemModel

class WeaknessesViewHolder(
    private val binding: ListItemWeaknessesBinding,
    @ColorInt
    private val color: Int
) : BaseViewHolder<WeaknessItemModel>(binding.root) {

    private val adapter by lazy { WeaknessAdapter() }

    init {
        binding.tvTitle.setTextColor(color)
        binding.rvWeakness.layoutManager = GridLayoutManager(itemView.context, 3)
        binding.rvWeakness.adapter = adapter
        binding.rvWeakness.setHasFixedSize(true)
        binding.rvWeakness.isNestedScrollingEnabled = false
    }

    override fun bind(model: WeaknessItemModel) {
        binding.tvTitle.text = model.title
        adapter.update(model.items)
    }
}
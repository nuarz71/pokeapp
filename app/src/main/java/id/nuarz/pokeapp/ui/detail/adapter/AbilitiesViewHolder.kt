package id.nuarz.pokeapp.ui.detail.adapter

import androidx.annotation.ColorInt
import androidx.recyclerview.widget.LinearLayoutManager
import id.nuarz.pokeapp.R
import id.nuarz.pokeapp.core.BaseViewHolder
import id.nuarz.pokeapp.databinding.ListItemAbilitiesBinding
import id.nuarz.pokeapp.ui.detail.AbilitiesItemModel
import id.nuarz.pokeapp.ui.detail.adapter.decorator.AbilitiesItemDecorator

class AbilitiesViewHolder(
    private val binding: ListItemAbilitiesBinding,
    @ColorInt
    color: Int
) : BaseViewHolder<AbilitiesItemModel>(binding.root) {

    private val adapter by lazy { AbilitiesAdapter(color) }

    init {
        binding.tvTitle.setTextColor(color)
        binding.tvTitle.text = itemView.context.getString(R.string.item_title_abilities)

        binding.rvAbilities.setHasFixedSize(true)
        binding.rvAbilities.isNestedScrollingEnabled = false
        binding.rvAbilities.layoutManager = LinearLayoutManager(itemView.context)
        binding.rvAbilities.adapter = adapter
        binding.rvAbilities.addItemDecoration(AbilitiesItemDecorator(itemView.context))
    }

    override fun bind(model: AbilitiesItemModel) {
        adapter.update(model.items)
    }
}
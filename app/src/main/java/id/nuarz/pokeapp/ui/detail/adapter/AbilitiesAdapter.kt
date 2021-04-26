package id.nuarz.pokeapp.ui.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import id.nuarz.pokeapp.core.BaseViewHolder
import id.nuarz.pokeapp.databinding.ListItemAblityDetailBinding
import id.nuarz.pokeapp.ui.detail.AbilityItemModel

class AbilitiesAdapter(
    @ColorInt
    private val color: Int
) : RecyclerView.Adapter<AbilitiesAdapter.ItemViewHolder>() {

    private val items = mutableListOf<AbilityItemModel>()

    fun update(list: List<AbilityItemModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ListItemAblityDetailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ItemViewHolder(
        private val binding: ListItemAblityDetailBinding
    ) : BaseViewHolder<AbilityItemModel>(binding.root) {

        init {
            binding.tvTitle.setTextColor(color)
        }

        override fun bind(model: AbilityItemModel) {
            binding.tvTitle.text = model.title
            binding.tvOverview.text = model.overview
        }
    }
}
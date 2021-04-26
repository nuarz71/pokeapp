package id.nuarz.pokeapp.ui.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import id.nuarz.pokeapp.core.BaseViewHolder
import id.nuarz.pokeapp.databinding.ListItemWeaknessTypeBinding
import id.nuarz.pokeapp.ui.detail.ElementWeaknessItemModel

class WeaknessAdapter : RecyclerView.Adapter<WeaknessAdapter.ItemViewHolder>() {

    private val items = mutableListOf<ElementWeaknessItemModel>()

    fun update(list: List<ElementWeaknessItemModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ListItemWeaknessTypeBinding.inflate(
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
        private val binding: ListItemWeaknessTypeBinding
    ) : BaseViewHolder<ElementWeaknessItemModel>(binding.root) {
        override fun bind(model: ElementWeaknessItemModel) {
            binding.ivIcon.setImageResource(model.iconResId)
            binding.ivIcon.setBackgroundColor(
                ContextCompat.getColor(
                    itemView.context,
                    model.colorResId
                )
            )
            binding.tvEffect.text = model.effect ?: "0"
        }
    }
}
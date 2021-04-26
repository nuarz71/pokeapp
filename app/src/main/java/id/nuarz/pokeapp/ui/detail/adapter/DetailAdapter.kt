package id.nuarz.pokeapp.ui.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.nuarz.pokeapp.databinding.*
import id.nuarz.pokeapp.ui.detail.*

class DetailAdapter(
    private val color: Int,
    private val lightColor: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_STAT = 1
        const val VIEW_TYPE_WEAKNESS = 2
        const val VIEW_TYPE_ABILITIES = 3
        const val VIEW_TYPE_BREEDING = 4
        const val VIEW_TYPE_CAPTURE = 5
        const val VIEW_TYPE_SPRITES = 6
        const val VIEW_TYPE_EVOLUTION = 7
        const val VIEW_TYPE_LOADING = 8
        const val VIEW_TYPE_CONN_ERR = 9
    }

    private val items = mutableListOf<DetailUiModel>()

    fun update(list: List<DetailUiModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun loading() {
        items.clear()
        items.add(LoadingItemModel)
        notifyDataSetChanged()
    }

    fun connectionError() {
        items.clear()
        items.add(ConnectionError)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_STAT -> StatViewHolder(
                ListItemStatBinding.inflate(inflater, parent, false),
                color
            )
            VIEW_TYPE_WEAKNESS -> WeaknessesViewHolder(
                ListItemWeaknessesBinding.inflate(inflater, parent, false),
                color
            )
            VIEW_TYPE_ABILITIES -> AbilitiesViewHolder(
                ListItemAbilitiesBinding.inflate(inflater, parent, false),
                color
            )
            VIEW_TYPE_BREEDING -> BreedingViewHolder(
                ListItemBreedingCaptureBinding.inflate(inflater, parent, false),
                color
            )
            VIEW_TYPE_CAPTURE -> CaptureViewHolder(
                ListItemBreedingCaptureBinding.inflate(inflater, parent, false),
                color
            )
            VIEW_TYPE_SPRITES -> SpritesViewHolder(
                ListItemSpritesBinding.inflate(inflater, parent, false),
                color,
                lightColor
            )
            VIEW_TYPE_EVOLUTION -> EvolutionViewHolder(
                ListItemEvolutionBinding.inflate(inflater, parent, false),
                color
            )
            VIEW_TYPE_LOADING -> LoadingViewHolder(
                ListItemLoadingBinding.inflate(inflater, parent, false),
                color
            )
            VIEW_TYPE_CONN_ERR -> ConnectionErrorViewHolder(
                ListItemConnectionErrorBinding.inflate(inflater, parent, false),
            )
            else -> throw IllegalStateException("Unsupported view type $viewType ")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_STAT -> {
                (holder as StatViewHolder).bind(items[position] as StatItemModel)
            }
            VIEW_TYPE_WEAKNESS -> {
                (holder as WeaknessesViewHolder).bind(items[position] as WeaknessItemModel)
            }
            VIEW_TYPE_ABILITIES -> {
                (holder as AbilitiesViewHolder).bind(items[position] as AbilitiesItemModel)
            }
            VIEW_TYPE_BREEDING -> {
                (holder as BreedingViewHolder).bind(items[position] as BreedingItemModel)
            }
            VIEW_TYPE_CAPTURE -> {
                (holder as CaptureViewHolder).bind(items[position] as CaptureItemModel)
            }
            VIEW_TYPE_SPRITES -> {
                (holder as SpritesViewHolder).bind(items[position] as SpritesItemModel)
            }
            VIEW_TYPE_EVOLUTION -> {
                (holder as EvolutionViewHolder).bind(items[position] as EvolutionItemModel)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is StatItemModel -> VIEW_TYPE_STAT
            is WeaknessItemModel -> VIEW_TYPE_WEAKNESS
            is AbilitiesItemModel -> VIEW_TYPE_ABILITIES
            is BreedingItemModel -> VIEW_TYPE_BREEDING
            is CaptureItemModel -> VIEW_TYPE_CAPTURE
            is SpritesItemModel -> VIEW_TYPE_SPRITES
            is EvolutionItemModel -> VIEW_TYPE_EVOLUTION
            LoadingItemModel -> VIEW_TYPE_LOADING
            ConnectionError -> VIEW_TYPE_CONN_ERR
            else -> throw IllegalStateException("Unsupported item ${items[position]::class}, required ${DetailUiModel::class} ")
        }
    }
}
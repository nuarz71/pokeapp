package id.nuarz.pokeapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import id.nuarz.pokeapp.R
import id.nuarz.pokeapp.core.BaseViewHolder
import id.nuarz.pokeapp.databinding.ListItemConnectionErrorBinding
import id.nuarz.pokeapp.databinding.ListItemLoadingBinding
import id.nuarz.pokeapp.databinding.ListItemPokemonBinding
import id.nuarz.pokeapp.ui.detail.ConnectionError
import id.nuarz.pokeapp.ui.detail.DetailUiModel
import id.nuarz.pokeapp.ui.detail.LoadingItemModel
import id.nuarz.pokeapp.ui.detail.adapter.ConnectionErrorViewHolder
import id.nuarz.pokeapp.ui.detail.adapter.LoadingViewHolder

class ListPokemonAdapter(private val onClick: (PokemonItemModel, Navigator.Extras?) -> Unit = { _, _ -> }) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_POKEMON = 1
        const val VIEW_TYPE_LOADING = 2
        const val VIEW_TYPE_CONN_ERR = 3
    }

    private val items = mutableListOf<HomeUiModel>()

    fun updateList(data: List<HomeUiModel>) {
        items.clear()
        items.addAll(data)
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
            VIEW_TYPE_POKEMON -> PokemonViewHolder(
                ListItemPokemonBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                onClick
            )
            VIEW_TYPE_LOADING -> LoadingViewHolder(
                ListItemLoadingBinding.inflate(inflater, parent, false),
                ContextCompat.getColor(parent.context, R.color.color_type_dragon)
            )
            VIEW_TYPE_CONN_ERR -> ConnectionErrorViewHolder(
                ListItemConnectionErrorBinding.inflate(inflater, parent, false),
            )
            else -> throw IllegalStateException("Unsupported view type $viewType ")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_POKEMON -> {
                (holder as PokemonViewHolder).bind(items[position] as PokemonItemModel)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is PokemonItemModel -> VIEW_TYPE_POKEMON
            LoadingItemModel -> VIEW_TYPE_LOADING
            ConnectionError -> VIEW_TYPE_CONN_ERR
            else -> throw IllegalStateException("Unsupported item ${items[position]::class}, required ${DetailUiModel::class} ")
        }
    }

    class PokemonViewHolder(
        private val binding: ListItemPokemonBinding,
        private val click: (PokemonItemModel, Navigator.Extras?) -> Unit
    ) : BaseViewHolder<PokemonItemModel>(binding.root) {

        override fun bind(model: PokemonItemModel) {

            itemView.setOnClickListener {
                val extra = FragmentNavigatorExtras(
                    binding.ivAvatar to "avatar_transform",
                    binding.tvName to "title_transform"
                )
                click(model, extra)
            }

            binding.tvName.text = model.name
            binding.tvNumber.text = model.number

            Glide.with(binding.ivAvatar)
                .load(model.imageUrl)
                .thumbnail(0.5f)
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(binding.ivAvatar)

            when {
                model.elements.isNotEmpty() && model.elements.size > 1 -> {
                    binding.ivType2.visibility = View.VISIBLE
                    binding.ivType1.visibility = View.VISIBLE

                    val icon1 = model.elements[0].iconResId
                    val color1 = ContextCompat.getColor(
                        itemView.context,
                        model.elements[0].colorResId
                    )

                    binding.ivType1.setImageResource(icon1)
                    binding.ivType1.setBackgroundColor(color1)

                    val icon2 = model.elements[1].iconResId
                    val color2 = ContextCompat.getColor(
                        itemView.context,
                        model.elements[1].colorResId
                    )
                    binding.ivType2.setImageResource(icon2)
                    binding.ivType2.setBackgroundColor(color2)
                }

                model.elements.isNotEmpty() -> {
                    binding.ivType2.visibility = View.GONE
                    binding.ivType1.visibility = View.VISIBLE

                    val icon1 = model.elements[0].iconResId
                    val color1 = ContextCompat.getColor(
                        itemView.context,
                        model.elements[0].colorResId
                    )
                    binding.ivType1.setImageResource(icon1)
                    binding.ivType1.setBackgroundColor(color1)
                }
                else -> {
                    binding.ivType2.visibility = View.GONE
                    binding.ivType1.visibility = View.GONE
                }
            }
        }
    }
}
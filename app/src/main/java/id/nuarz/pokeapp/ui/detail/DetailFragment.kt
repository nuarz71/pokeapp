package id.nuarz.pokeapp.ui.detail

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.nuarz.pokeapp.R
import id.nuarz.pokeapp.core.BaseFragment
import id.nuarz.pokeapp.core.ext.alphaColor
import id.nuarz.pokeapp.core.ext.observe
import id.nuarz.pokeapp.databinding.FragmentDetailBinding
import id.nuarz.pokeapp.ui.detail.adapter.DetailAdapter
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>(FragmentDetailBinding::inflate) {

    private val args by navArgs<DetailFragmentArgs>()

    private val viewModel: DetailViewModel by viewModels()

    @Inject
    lateinit var elementTypes: Map<String, Pair<Int, Int>>

    private lateinit var detailAdapter: DetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(
            android.R.transition.move
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        @Suppress("DEPRECATION")
        view.systemUiVisibility = SYSTEM_UI_FLAG_LAYOUT_STABLE or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        val firstColor = ContextCompat.getColor(
            requireContext(), args.pokemonItemModel.elements[0].colorResId
        )
        val secondColor = firstColor.alphaColor(0.65F)

        Glide.with(binding.ivAvatar)
            .load(args.pokemonItemModel.imageUrl)
            .thumbnail(0.5f)
            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
            .into(binding.ivAvatar)

        binding.tvTitle.text = args.pokemonItemModel.name

        if (args.pokemonItemModel.elements.isEmpty()) return
        binding.llElements.removeAllViews()
        args.pokemonItemModel.elements.forEach { type ->
            binding.llElements.post {
                val child = LayoutInflater.from(requireContext()).inflate(
                    R.layout.button_item_detail_element,
                    binding.llElements,
                    false
                ) as MaterialButton
                val id = View.generateViewId()
                child.id = id
                val color = ContextCompat.getColor(
                    requireContext(),
                    type.colorResId
                )
                child.setIconResource(type.iconResId)
                child.text = type.name
                child.backgroundTintList = ColorStateList.valueOf(color)
                binding.llElements.addView(child)
            }
        }
        (binding.constrainLayout.background as GradientDrawable).also {
            it.colors = intArrayOf(secondColor, firstColor)
        }

        val buttonBackgroundStateList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_enabled),
                intArrayOf(-android.R.attr.state_enabled)
            ),
            intArrayOf(
                Color.WHITE,
                firstColor
            )
        )
        val buttonTextStateList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_enabled),
                intArrayOf(-android.R.attr.state_enabled)
            ),
            intArrayOf(
                firstColor,
                Color.WHITE
            )
        )
        binding.btnStat.backgroundTintList = buttonBackgroundStateList
        binding.btnStat.setTextColor(buttonTextStateList)
        binding.btnStat.setOnClickListener {
            binding.btnStat.isEnabled = false
            binding.btnEvolutions.isEnabled = true
            viewModel.onEvent(Event.StatClick)
        }

        binding.btnEvolutions.backgroundTintList = buttonBackgroundStateList
        binding.btnEvolutions.setTextColor(buttonTextStateList)
        binding.btnEvolutions.setOnClickListener {
            binding.btnStat.isEnabled = true
            binding.btnEvolutions.isEnabled = false
            viewModel.onEvent(Event.EvolutionClick)
        }

        binding.btnClose.setOnClickListener {
            navController.popBackStack()
        }

        if (!::detailAdapter.isInitialized) {
            detailAdapter = DetailAdapter(firstColor, secondColor)
        }

        binding.rvDetails.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDetails.adapter = detailAdapter

        bindViewModel()

        viewModel.onEvent(Event.OnViewCreated(args.pokemonItemModel.id))
    }

    private fun bindViewModel() = observe(viewModel.state) {
        when (it) {
            State.DetailStatState.Loading -> detailAdapter.loading()
            is State.DetailStatState.Failed -> {
                if (detailAdapter.itemCount <= 1) {
                    detailAdapter.clear()
                }
                showSnackBar(it.message ?: "Unknown Error")
            }
            is State.DetailStatState.Loaded -> {
                if (!binding.btnStat.isEnabled)
                    updateUi(it.overview, it)
            }
            is State.EvolutionState.Loading -> detailAdapter.loading()
            is State.EvolutionState.Failed -> {
                if (detailAdapter.itemCount <= 1) {
                    detailAdapter.clear()
                }
                showSnackBar(it.message ?: "Unknown Error")
            }
            is State.EvolutionState.Loaded -> {
                if (!binding.btnEvolutions.isEnabled)
                    detailAdapter.update(it.list)
            }
            is State.ConnectionFailed -> {
                detailAdapter.connectionError()
                showSnackBar(getString(R.string.label_connection_trouble))
            }
        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            requireContext(),
            binding.root,
            message,
            Snackbar.LENGTH_LONG
        ).apply {
            setAction("Retry") {
                val event = if (binding.btnEvolutions.isEnabled) {
                    Event.RetryEvolution
                } else {
                    Event.RetryStat
                }
                viewModel.onEvent(event)
                dismiss()
            }
            show()
        }
    }

    private fun updateUi(overview: String?, state: State.DetailStatState.Loaded) {
        binding.tvOverview.text = overview
        detailAdapter.update(state.list)
    }
}
package id.nuarz.pokeapp.ui.detail

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.nuarz.pokeapp.R
import id.nuarz.pokeapp.core.BaseFragment
import id.nuarz.pokeapp.core.ext.alphaColor
import id.nuarz.pokeapp.core.ext.observe
import id.nuarz.pokeapp.databinding.FragmentDetailBinding
import id.nuarz.pokeapp.ui.detail.adapter.DetailAdapter

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>(FragmentDetailBinding::inflate) {

    private val args by navArgs<DetailFragmentArgs>()

    private val viewModel: DetailViewModel by viewModels()

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

        configureHeaderView(firstColor, secondColor)
        configureViewElements()
        configureButtonTabs(firstColor)
        configureRecyclerView(firstColor, secondColor)

        bindViewModel()

        viewModel.onEvent(Event.OnViewCreated(args.pokemonItemModel.id))
    }

    private fun configureHeaderView(firstColor: Int, secondColor: Int) {
        Glide.with(binding.ivAvatar)
            .load(args.pokemonItemModel.imageUrl)
            .thumbnail(0.5f)
            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
            .into(binding.ivAvatar)
        binding.tvTitle.text = args.pokemonItemModel.name
        (binding.constrainLayout.background as GradientDrawable).also {
            it.colors = intArrayOf(secondColor, firstColor)
        }
        binding.btnClose.setOnClickListener {
            navController.popBackStack()
        }
    }

    private fun configureRecyclerView(@ColorInt firstColor: Int, @ColorInt secondColor: Int) {
        if (!::detailAdapter.isInitialized) {
            detailAdapter = DetailAdapter(firstColor, secondColor)
        }
        binding.rvDetails.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDetails.adapter = detailAdapter
    }

    private fun configureButtonTabs(@ColorInt firstColor: Int) {
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
    }

    private fun configureViewElements() {
        if (args.pokemonItemModel.elements.isEmpty()) return
        binding.llElements.removeAllViews()
        args.pokemonItemModel.elements.forEach { type ->
            binding.llElements.post {
                val child = LayoutInflater.from(requireContext()).inflate(
                    R.layout.item_detail_element,
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
    }

    private fun bindViewModel() = observe(viewModel.state) {
        when (it) {
            State.DetailStatState.Loading -> updateStatUiLoading()
            is State.DetailStatState.Failed -> updateStatUiError(it)
            is State.DetailStatState.Loaded -> updateStatUiDetail(it.overview, it.list)
            is State.EvolutionState.Loading -> updateEvolutionUiLoading()
            is State.EvolutionState.Failed -> updateEvolutionUiError(it)
            is State.EvolutionState.Loaded -> updateEvolutionUiDetail(it.list)
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

    private fun updateStatUiDetail(overview: String?, items: List<DetailUiModel>) {
        if (binding.btnStat.isEnabled) return

        binding.tvOverview.text = overview
        detailAdapter.update(items)
    }

    private fun updateStatUiLoading() {
        if (binding.btnStat.isEnabled) return
        detailAdapter.loading()
    }

    private fun updateStatUiError(state: State.DetailStatState.Failed) {
        if (binding.btnStat.isEnabled) return

        if (detailAdapter.itemCount <= 1) {
            detailAdapter.clear()
        }
        showSnackBar(state.message ?: "Unknown Error")
    }

    private fun updateEvolutionUiDetail(items: List<DetailUiModel>) {
        if (binding.btnEvolutions.isEnabled) return
        detailAdapter.update(items)
    }

    private fun updateEvolutionUiError(state: State.EvolutionState.Failed) {
        if (binding.btnEvolutions.isEnabled) return
        if (detailAdapter.itemCount <= 1) {
            detailAdapter.clear()
        }
        showSnackBar(state.message ?: "Unknown Error")
    }

    private fun updateEvolutionUiLoading() {
        if (binding.btnEvolutions.isEnabled) return
        detailAdapter.loading()
    }
}
package id.nuarz.pokeapp.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.Navigator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.nuarz.pokeapp.core.ext.observe
import id.nuarz.pokeapp.core.ext.toast
import id.nuarz.pokeapp.databinding.FragmentHomeBinding
import id.nuarz.pokeapp.core.BaseFragment

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: HomeViewModel by viewModels()

    private val adapter by lazy {
        ListPokemonAdapter() { model, extra ->
            onItemClick(model, extra)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(
            android.R.transition.move
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvPokemon.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPokemon.adapter = adapter
        binding.rvPokemon.addItemDecoration(HomeItemDecorator(requireContext()))

        viewModel.onEvent(Event.OnViewCreate)

        observe(viewModel.state) {
            when (it) {
                State.Loading -> adapter.loading()
                is State.Loaded -> adapter.updateList(it.items)
                is State.Failed -> Snackbar.make(
                    requireContext(),
                    binding.root,
                    it.message ?: "Unknown Error",
                    Snackbar.LENGTH_LONG
                ).show()
                State.ConnectionError -> adapter.connectionError()
            }
        }
    }

    private fun onItemClick(model: PokemonItemModel, extras: Navigator.Extras?) {
        val directions = HomeFragmentDirections.actionHomeFragmentToDetailFragment(model)
        if (extras != null) {
            navController.navigate(directions, extras)
        } else {
            navController.navigate(directions)
        }
    }
}
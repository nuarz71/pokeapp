package id.nuarz.pokeapp.ui.splash

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import id.nuarz.pokeapp.R
import id.nuarz.pokeapp.databinding.FragmentSplashBinding
import id.nuarz.pokeapp.core.BaseFragment
import kotlinx.coroutines.delay

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            delay(1500L)
            if (view == null) return@launchWhenResumed
            navController.navigate(R.id.action_splashFragment_to_homeFragment)
        }
    }
}
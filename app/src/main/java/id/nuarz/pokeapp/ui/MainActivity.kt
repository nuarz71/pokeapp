package id.nuarz.pokeapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import id.nuarz.pokeapp.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!::navController.isInitialized) {
            val navHostFragment = supportFragmentManager.findFragmentById(binding.containerView.id)
                    as? NavHostFragment ?: throw IllegalStateException("NavHost not found")
            navController = navHostFragment.navController
        }
    }
}
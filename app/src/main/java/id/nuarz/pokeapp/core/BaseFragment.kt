package id.nuarz.pokeapp.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding

typealias ViewBindingInflater<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB : ViewBinding>(
    private val viewBindingInflate: ViewBindingInflater<VB>
) : Fragment() {

    private var currentBinding: VB? = null
    protected val binding: VB
        get() = currentBinding ?: throw IllegalStateException("ViewBinding not found")

    protected val navController: NavController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        currentBinding = viewBindingInflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        currentBinding = null
    }
}
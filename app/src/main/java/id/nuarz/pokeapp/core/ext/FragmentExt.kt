package id.nuarz.pokeapp.core.ext

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData

inline fun <T> Fragment.observe(data: LiveData<T>, crossinline block: (T) -> Unit) {
    data.observe(viewLifecycleOwner, { block(it) })
}

@JvmOverloads
fun Fragment.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    requireContext().toast(message, duration)
}
package id.nuarz.pokeapp.core.ext

import android.content.Context
import android.widget.Toast

@JvmOverloads
fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}
package id.nuarz.pokeapp.core.ext

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange

@ColorInt
fun Int.alphaColor(@FloatRange(from = 0.0, to = 1.0) offset: Float): Int {
    val alpha = (Color.alpha(this) * offset).toInt()
    val red = Color.red(this)
    val green = Color.green(this)
    val blue = Color.blue(this)

    return Color.argb(alpha, red, green, blue)
}
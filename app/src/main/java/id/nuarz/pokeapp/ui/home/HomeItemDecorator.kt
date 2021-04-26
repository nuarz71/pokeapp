package id.nuarz.pokeapp.ui.home

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import id.nuarz.pokeapp.R
import kotlin.math.roundToInt

class HomeItemDecorator(context: Context) : RecyclerView.ItemDecoration() {

    private val density = Resources.getSystem().displayMetrics.density
    private val divider = ContextCompat.getDrawable(context, R.drawable.shape_item_divider)

    private val bounds = Rect()

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager == null || divider == null) return
        canvas.save()
        val startMargin = (16 * density).roundToInt()
        for (index in 0 until parent.childCount - 1) {
            val child = parent.getChildAt(index)
            parent.getDecoratedBoundsWithMargins(child, bounds)
            val bottom = bounds.bottom + child.translationY.roundToInt()
            val top = bottom - divider.intrinsicHeight
            val left: Int = child.left + startMargin
            val right: Int = child.right
            divider.setBounds(left, top, right, bottom)
            divider.draw(canvas)
        }
        canvas.restore()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.set(0, 0, 0, 0 + (divider?.intrinsicHeight ?: 0))
    }
}
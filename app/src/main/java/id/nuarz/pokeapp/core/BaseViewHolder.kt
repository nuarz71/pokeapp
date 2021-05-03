package id.nuarz.pokeapp.core

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder<in UiModel>(view: View) : RecyclerView.ViewHolder(view) {

    protected val context: Context
        get() = itemView.context

    abstract fun bind(model: UiModel)
}
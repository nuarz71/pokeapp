package id.nuarz.pokeapp.core

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder<in UiModel>(view: View) : RecyclerView.ViewHolder(view) {

    abstract fun bind(model: UiModel)
}
package id.nuarz.pokeapp.ui.detail.adapter

import id.nuarz.pokeapp.core.BaseViewHolder
import id.nuarz.pokeapp.databinding.ListItemStatBinding
import id.nuarz.pokeapp.ui.detail.StatItemModel

class StatViewHolder(
    private val binding: ListItemStatBinding,
    color: Int
) : BaseViewHolder<StatItemModel>(binding.root) {

    init {
        binding.tvStatLabelHP.setTextColor(color)
        binding.progressHP.setIndicatorColor(color)

        binding.tvStatLabelATK.setTextColor(color)
        binding.progressATK.setIndicatorColor(color)

        binding.tvStatLabelDEF.setTextColor(color)
        binding.progressDEF.setIndicatorColor(color)

        binding.tvStatLabelSATK.setTextColor(color)
        binding.progressSATK.setIndicatorColor(color)

        binding.tvStatLabelSDEF.setTextColor(color)
        binding.progressSDEF.setIndicatorColor(color)

        binding.tvStatLabelSPD.setTextColor(color)
        binding.progressSPD.setIndicatorColor(color)
    }

    override fun bind(model: StatItemModel) {
        binding.tvStatValueHP.text = String.format("%03d", model.hp)
        binding.progressHP.progress = model.hp

        binding.tvStatValueATK.text = String.format("%03d", model.atk)
        binding.progressATK.progress = model.atk

        binding.tvStatValueDEF.text = String.format("%03d", model.def)
        binding.progressDEF.progress = model.def

        binding.tvStatValueSATK.text = String.format("%03d", model.satk)
        binding.progressSATK.progress = model.satk

        binding.tvStatValueSDEF.text = String.format("%03d", model.sdef)
        binding.progressSDEF.progress = model.sdef

        binding.tvStatValueSPD.text = String.format("%03d", model.spd)
        binding.progressSPD.progress = model.spd
    }
}
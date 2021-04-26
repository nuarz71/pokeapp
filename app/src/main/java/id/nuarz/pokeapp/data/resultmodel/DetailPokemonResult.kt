package id.nuarz.pokeapp.data.resultmodel

import id.nuarz.pokeapp.data.model.PokemonStat

data class DetailPokemonResult(
    val evolutionId : Int,
    val overview: String?,
    val stat: StatResult,
    val abilities: List<NameAndOverview>,
    val breeding: BreedingResult,
    val capture : CaptureResult
)

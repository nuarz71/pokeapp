package id.nuarz.pokeapp.data

import id.nuarz.pokeapp.data.resultmodel.DetailPokemonResult
import id.nuarz.pokeapp.data.resultmodel.EvolutionResult
import id.nuarz.pokeapp.data.resultmodel.PokemonResult
import id.nuarz.pokeapp.data.resultmodel.ResultState
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun listPokemon(offset: Int): Flow<ResultState<List<PokemonResult>>>

    suspend fun detail(id: Int): Flow<ResultState<DetailPokemonResult>>

    suspend fun evolution(
        pokemonId: Int,
        evolutionId: Int?
    ): Flow<ResultState<List<EvolutionResult>>>
}
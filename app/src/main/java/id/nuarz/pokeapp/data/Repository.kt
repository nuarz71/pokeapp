package id.nuarz.pokeapp.data

import id.nuarz.pokeapp.data.resultmodel.DetailPokemonResult
import id.nuarz.pokeapp.data.resultmodel.EvolutionResult
import id.nuarz.pokeapp.data.resultmodel.PokemonResult
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun listPokemon(offset: Int): Flow<List<PokemonResult>>

    suspend fun detail(id: Int): Flow<DetailPokemonResult>

    suspend fun evolution(
        pokemonId: Int,
        evolutionId: Int?
    ): Flow<List<EvolutionResult>>
}
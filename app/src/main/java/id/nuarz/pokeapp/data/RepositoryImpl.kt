package id.nuarz.pokeapp.data

import id.nuarz.pokeapp.R
import id.nuarz.pokeapp.core.ext.clearWhiteSpace
import id.nuarz.pokeapp.data.entity.*
import id.nuarz.pokeapp.data.model.Evolve
import id.nuarz.pokeapp.data.model.NameAndUrl
import id.nuarz.pokeapp.data.model.PokemonAbility
import id.nuarz.pokeapp.data.resultmodel.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okio.IOException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.*
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val service: PokemonService,
    private val elementTypes: Map<String, Pair<Int, Int>>,
    private val dao: PokemonDao
) : Repository {

    private fun String.urlLastToId(): Int {
        return split("/".toRegex()).dropLast(1).last().toInt()
    }

    override suspend fun listPokemon(offset: Int): Flow<ResultState<List<PokemonResult>>> = flow {
        emit(ResultState.Loading)
        coroutineScope {
            try {
                //async request from network
                val requestPokemon = async { service.listPokemon(offset) }

                //get from local
                val data = async { dao.listPokemon() }
                if (data.await().isNotEmpty()) {
                    val list = data.await().map {
                        PokemonResult(
                            it.pokemon.id,
                            it.pokemon.name,
                            it.pokemon.displayName,
                            it.elements.map { type ->
                                ElementType(
                                    type.name,
                                    elementTypes[type.name]?.first ?: R.drawable.ic_type_normal,
                                    elementTypes[type.name]?.second
                                        ?: R.color.color_type_normal,
                                )
                            }
                        )
                    }

                    //emmit result from local
                    emit(ResultState.Loaded(list))
                }

                //load element (type) each item from network and map it to result
                val pokemons = requestElementPokemon(requestPokemon.await().result)

                //prepare save to local
                val pokemonEntities = pokemons.map {
                    PokemonEntity(it.id, it.name, it.displayName)
                }

                //save toLocal
                val savePokemons = async { dao.saveAllPokemon(pokemonEntities) }
                val saveElements = async {
                    val elementEntities = mutableListOf<ElementEntity>()
                    pokemons.forEach {
                        elementEntities.addAll(it.elements.map { element ->
                            ElementEntity(it.id, element.name)
                        })
                    }
                    dao.deleteAllElements()
                    dao.saveElements(elementEntities)
                }
                savePokemons.await()
                saveElements.await()

                //emit result from network
                emit(ResultState.Loaded(pokemons))
            } catch (e: Throwable) {
                emit(ResultState.Failed.Error(e.message))
            }
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun requestElementPokemon(list: List<NameAndUrl>): List<PokemonResult> {
        return list.map {
            val id = it.url.urlLastToId()
            val displayName = it.name.capitalize(Locale.getDefault())

            val types = service.getTypesById(id).types.map { type ->
                ElementType(
                    type.type.name,
                    elementTypes[type.type.name]?.first ?: R.drawable.ic_type_normal,
                    elementTypes[type.type.name]?.second ?: R.color.color_type_normal,
                )
            }

            PokemonResult(id, it.name, displayName, types)
        }
    }

    override suspend fun detail(id: Int): Flow<ResultState<DetailPokemonResult>> = flow {
        emit(ResultState.Loading)
        coroutineScope {
            try {
                val reqPokemon = async { service.getPokemonById(id) }
                val reqSpecies = async { service.getSpeciesById(id) }

                val localDetail = async { getDetailFromLocal(id) }
                if (localDetail.await() != null) {
                    emit(ResultState.Loaded(localDetail.await()!!))
                }

                val pokemon = reqPokemon.await()
                val species = reqSpecies.await()

                val overview = species.flavorTextEntries.firstOrNull {
                    it.language.name == "en"
                }?.flavorText?.clearWhiteSpace()

                val abilities = async { requestAbilities(pokemon.abilities) }
                val eggGroups = async { requestEggGroups(species.eggGroups) }

                val habitatName = async { requestHabitat(species.habitat.url.urlLastToId()) }
                val generationName = async {
                    requestGeneration(species.generation.url.urlLastToId())
                }

                val evolutionId = species.evolutionChain.url.urlLastToId()
                val detailResult = DetailPokemonResult(
                    evolutionId,
                    overview,
                    StatResult(
                        pokemon.stat[0].baseStat ?: 0,
                        pokemon.stat[1].baseStat ?: 0,
                        pokemon.stat[2].baseStat ?: 0,
                        pokemon.stat[3].baseStat ?: 0,
                        pokemon.stat[4].baseStat ?: 0,
                        pokemon.stat[5].baseStat ?: 0
                    ),
                    abilities.await(),
                    BreedingResult(
                        eggGroups.await(),
                        species.hatchCounter,
                        species.genderRate
                    ),
                    CaptureResult(
                        habitatName.await(),
                        generationName.await(),
                        species.captureRate
                    )
                )

                //save to local
                val saveDetail = async {
                    dao.deleteDetailByPokemonId(id)
                    dao.saveDetail(
                        DetailEntity(
                            pokemon.id,
                            overview,
                            species.hatchCounter, species.genderRate,
                            habitatName.await(),
                            generationName.await(),
                            species.captureRate,
                            evolutionId
                        )
                    )
                }
                val saveAbility = async {
                    dao.deleteAbilityByPokemonId(id)
                    dao.saveAllAbility(abilities.await().map {
                        AbilityEntity(id, it.name, it.overview)
                    })
                }

                val saveEggGroup = async {
                    dao.deleteEggGroupByPokemonId(id)
                    dao.saveAllEggGroup(eggGroups.await().map {
                        EggGroupEntity(id, it)
                    })
                }

                val saveStat = async {
                    dao.deleteStatByPokemonId(id)
                    dao.saveStat(
                        StatEntity(
                            id,
                            pokemon.stat[0].baseStat ?: 0,
                            pokemon.stat[1].baseStat ?: 0,
                            pokemon.stat[2].baseStat ?: 0,
                            pokemon.stat[3].baseStat ?: 0,
                            pokemon.stat[4].baseStat ?: 0,
                            pokemon.stat[5].baseStat ?: 0
                        )
                    )
                }

                saveDetail.await()
                saveAbility.await()
                saveEggGroup.await()
                saveStat.await()

                //emit result from network
                emit(ResultState.Loaded(detailResult))

            } catch (e: Throwable) {
                emit(handleError(e))
            }
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun getDetailFromLocal(id: Int): DetailPokemonResult? {
        val data = dao.getDetail(id)
        var result: DetailPokemonResult? = null
        if (data?.detail != null && data.stat != null && data.eggGroup.isNotEmpty() && data.abilities.isNotEmpty()) {
            result = DetailPokemonResult(
                data.detail.evolutionId,
                data.detail.overview,
                StatResult(
                    data.stat.hp,
                    data.stat.atk,
                    data.stat.def,
                    data.stat.satk,
                    data.stat.sdef,
                    data.stat.def
                ),
                data.abilities.map {
                    NameAndOverview(it.name, it.overview)
                },
                BreedingResult(
                    data.eggGroup.map {
                        it.name
                    },
                    data.detail.hatchTime,
                    data.detail.genderRate
                ),
                CaptureResult(
                    data.detail.habitat,
                    data.detail.generation,
                    data.detail.captureRate
                )
            )
        }
        return result
    }

    override suspend fun evolution(
        pokemonId: Int,
        evolutionId: Int?
    ): Flow<ResultState<List<EvolutionResult>>> = flow {
        emit(ResultState.Loading)
        coroutineScope {
            try {
                //Req from network
                val chain = service.getEvolutionChain(evolutionId!!)
                //Get from local
                val data = dao.getEvolutionByEvolutionId(evolutionId)
                if (data.isNotEmpty()) {
                    val result = data.map {
                        EvolutionResult(
                            it.fromName,
                            it.fromId,
                            it.toName,
                            it.toId,
                            it.trigger
                        )
                    }
                    //emit result from local
                    emit(ResultState.Loaded(result))
                }

                val list = mutableListOf<EvolutionResult>()
                var evolve: Evolve = chain.chain
                var find = true
                do {
                    val evolutionDetails = evolve.evolvesTo[0].evolutionDetails[0]
                    val evolutionTrigger = when (evolutionDetails.trigger.name) {
                        "level-up" -> {
                            when {
                                evolutionDetails.minLevel != null -> "Lv. ${evolutionDetails.minLevel}"
                                evolutionDetails.minHappiness != null -> "Lv. Up & Happy ${evolutionDetails.minHappiness}"
                                evolutionDetails.minBeauty != null -> "Lv. Up & Beauty ${evolutionDetails.minBeauty}"
                                evolutionDetails.minAffection != null -> "Lv. Up & Affect ${evolutionDetails.minAffection}"
                                else -> "Level Up"
                            }
                        }
                        "use-item" -> evolutionDetails.item?.name ?: "Use Item"
                        else -> "Level Up"
                    }
                    list.add(
                        EvolutionResult(
                            evolve.species.name.capitalize(Locale.getDefault()),
                            evolve.species.url.urlLastToId(),
                            evolve.evolvesTo[0].species.name.capitalize(Locale.getDefault()),
                            evolve.evolvesTo[0].species.url.urlLastToId(),
                            evolutionTrigger
                        )
                    )
                    if (evolve.evolvesTo[0].evolvesTo.isEmpty()) {
                        find = false
                    } else {
                        evolve = evolve.evolvesTo[0]
                    }
                } while (find)

                //save to local
                dao.saveAllEvolution(list.map {
                    EvolutionEntity(
                        pokemonId,
                        evolutionId,
                        it.from,
                        it.fromId,
                        it.to,
                        it.toId,
                        it.trigger
                    )
                })

                //emit result from network
                emit(ResultState.Loaded(list))

            } catch (e: Throwable) {
                emit(handleError(e))
            }
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun requestAbilities(abilitiesFromPokemon: List<PokemonAbility>): List<NameAndOverview> {
        return abilitiesFromPokemon.map {
            val id = it.ability.url.urlLastToId()
            val ability = service.getAbility(id)

            val formattedName = ability.names.firstOrNull() { name ->
                name.language.name == "en"
            }?.name ?: ability.name

            val abilityOverview = ability.flavorTextEntries.firstOrNull() { flavor ->
                flavor.language.name == "en"
            }?.flavorText?.clearWhiteSpace()

            NameAndOverview(formattedName, abilityOverview)
        }
    }

    private suspend fun requestEggGroups(groups: List<NameAndUrl>): List<String> {
        return groups.map {
            val groupId = it.url.urlLastToId()
            val group = service.getEggGroup(groupId)

            val name = group.names.firstOrNull() { names ->
                names.language.name == "en"
            }?.name ?: "Unknown"

            name
        }
    }

    private suspend fun requestHabitat(habitatId: Int): String {
        val habitat = service.getHabitat(habitatId)
        return habitat.names.firstOrNull() {
            it.language.name == "en"
        }?.name ?: habitat.name
    }

    private suspend fun requestGeneration(generationId: Int): String {
        val generation = service.getGeneration(generationId)
        return generation.names.firstOrNull() {
            it.language.name == "en"
        }?.name ?: generation.name
    }

    private fun handleError(e: Throwable): ResultState.Failed {
        return when (e) {
            is SocketTimeoutException -> ResultState.Failed.Error("Timeout")
            is IOException, is ConnectException -> ResultState.Failed.Connection
            is HttpException -> ResultState.Failed.Error("Something wrong")
            else -> ResultState.Failed.Error(e.message)
        }
    }
}
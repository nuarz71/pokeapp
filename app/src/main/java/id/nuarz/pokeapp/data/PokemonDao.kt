package id.nuarz.pokeapp.data

import androidx.room.*
import id.nuarz.pokeapp.data.entity.*

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAllPokemon(pokemon: List<PokemonEntity>)

    @Transaction
    @Query("SELECT * FROM pokemon_basic ORDER BY id ASC limit 0,15")
    fun listPokemon(): List<PokemonWithElement>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveElements(elements: List<ElementEntity>)

    @Transaction
    @Query("DELETE FROM pokemon_element")
    fun deleteAllElements()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveDetail(entity: DetailEntity)

    @Transaction
    @Query("SELECT * FROM detail WHERE pokemon_id = :id")
    fun getDetail(id: Int): DetailWithStatAbility?

    @Transaction
    @Query("DELETE FROM detail WHERE pokemon_id = :id")
    fun deleteDetailByPokemonId(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveStat(stat: StatEntity)

    @Transaction
    @Query("DELETE FROM stat WHERE pokemon_id = :id")
    fun deleteStatByPokemonId(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAllAbility(list: List<AbilityEntity>)

    @Transaction
    @Query("DELETE FROM ability WHERE pokemon_id = :id")
    fun deleteAbilityByPokemonId(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAllEggGroup(groups: List<EggGroupEntity>)

    @Transaction
    @Query("DELETE FROM egg_group WHERE pokemon_id = :id")
    fun deleteEggGroupByPokemonId(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAllEvolution(list: List<EvolutionEntity>)

    @Transaction
    @Query("SELECT * FROM evolution WHERE evolution_id = :id ORDER BY id ASC")
    fun getEvolutionByEvolutionId(id: Int): List<EvolutionEntity>

    @Transaction
    @Query("DELETE FROM evolution WHERE pokemon_id = :id")
    fun deleteEvolutionByPokemonId(id: Int)
}
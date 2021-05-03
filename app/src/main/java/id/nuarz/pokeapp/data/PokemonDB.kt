package id.nuarz.pokeapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import id.nuarz.pokeapp.data.entity.*

@Database(
    entities = [
        PokemonEntity::class,
        ElementEntity::class,
        DetailEntity::class,
        StatEntity::class,
        AbilityEntity::class,
        EggGroupEntity::class,
        EvolutionEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PokemonDB : RoomDatabase() {

    abstract val dao: PokemonDao

    companion object {
        private const val name = "pokemon.db"

        fun create(context: Context): PokemonDB {
            return Room.databaseBuilder(context, PokemonDB::class.java, name).build()
        }
    }
}
package id.nuarz.pokeapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "evolution")
data class EvolutionEntity(
    @ColumnInfo(name = "pokemon_id")
    val pokemonId: Int,
    @ColumnInfo(name = "evolution_id")
    val evolutionId: Int,
    @ColumnInfo(name = "from_name")
    val fromName: String,
    @ColumnInfo(name = "from_id")
    val fromId: Int,
    @ColumnInfo(name = "to_name")
    val toName: String,
    @ColumnInfo(name = "to_id")
    val toId: Int,
    @ColumnInfo(name = "trigger")
    val trigger: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

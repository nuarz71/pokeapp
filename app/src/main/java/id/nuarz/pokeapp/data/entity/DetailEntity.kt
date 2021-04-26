package id.nuarz.pokeapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "detail")
data class DetailEntity(
    @ColumnInfo(name = "pokemon_id")
    val pokemonId: Int,

    @ColumnInfo(name = "overview")
    val overview: String?,

    @ColumnInfo(name = "hatch_time")
    val hatchTime: Int,

    @ColumnInfo(name = "gender_rate")
    val genderRate: Int,

    @ColumnInfo(name = "habitat")
    val habitat: String,

    @ColumnInfo(name = "generation")
    val generation: String,

    @ColumnInfo(name = "capture_rate")
    val captureRate: Int,

    @ColumnInfo(name = "evolution_id")
    val evolutionId: Int,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

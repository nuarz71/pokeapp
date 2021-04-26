package id.nuarz.pokeapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "stat")
data class StatEntity(
    @ColumnInfo(name = "pokemon_id")
    val pokemonId: Int,

    @ColumnInfo(name = "hp")
    val hp: Int,

    @ColumnInfo(name = "atk")
    val atk: Int,

    @ColumnInfo(name = "def")
    val def: Int,

    @ColumnInfo(name = "satk")
    val satk: Int,

    @ColumnInfo(name = "sdef")
    val sdef: Int,

    @ColumnInfo(name = "spd")
    val spd: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

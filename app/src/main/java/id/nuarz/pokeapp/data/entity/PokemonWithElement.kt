package id.nuarz.pokeapp.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class PokemonWithElement(
    @Embedded
    val pokemon: PokemonEntity,

    @Relation(entity = ElementEntity::class, parentColumn = "id", entityColumn = "pokemon_id")
    val elements: List<ElementEntity>
)

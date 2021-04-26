package id.nuarz.pokeapp.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class DetailWithStatAbility(
    @Embedded val detail: DetailEntity?,
    @Relation(entity = EggGroupEntity::class, parentColumn = "id", entityColumn = "pokemon_id")
    val eggGroup: List<EggGroupEntity>,
    @Relation(entity = StatEntity::class, parentColumn = "id", entityColumn = "pokemon_id")
    val stat: StatEntity?,
    @Relation(entity = AbilityEntity::class, parentColumn = "id", entityColumn = "pokemon_id")
    val abilities: List<AbilityEntity>
)

package id.nuarz.pokeapp.app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.nuarz.pokeapp.PokemonDB
import id.nuarz.pokeapp.R
import id.nuarz.pokeapp.data.PokemonService
import id.nuarz.pokeapp.data.Repository
import id.nuarz.pokeapp.data.RepositoryImpl
import id.nuarz.pokeapp.data.di.DBModule
import id.nuarz.pokeapp.data.di.NetworkModule
import retrofit2.Retrofit

@Module(includes = [NetworkModule::class, DBModule::class])
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideElementTypeSource(): Map<String, Pair<Int, Int>> {
        return mapOf(
            "normal" to (R.drawable.ic_type_normal to R.color.color_type_normal),
            "fighting" to (R.drawable.ic_type_fighting to R.color.color_type_fighting),
            "dragon" to (R.drawable.ic_type_dragon to R.color.color_type_dragon),
            "flying" to (R.drawable.ic_type_flying to R.color.color_type_flying),
            "poison" to (R.drawable.ic_type_poison to R.color.color_type_poison),
            "ground" to (R.drawable.ic_type_ground to R.color.color_type_ground),
            "rock" to (R.drawable.ic_type_rock to R.color.color_type_rock),
            "bug" to (R.drawable.ic_type_bug to R.color.color_type_bug),
            "ghost" to (R.drawable.ic_type_ghost to R.color.color_type_ghost),
            "steel" to (R.drawable.ic_type_steel to R.color.color_type_steal),
            "fire" to (R.drawable.ic_type_fire to R.color.color_type_fire),
            "water" to (R.drawable.ic_type_water to R.color.color_type_water),
            "grass" to (R.drawable.ic_type_grass to R.color.color_type_grass),
            "electric" to (R.drawable.ic_type_electric to R.color.color_type_electric),
            "psychic" to (R.drawable.ic_type_psychic to R.color.color_type_physic),
            "ice" to (R.drawable.ic_type_ice to R.color.color_type_ice),
            "dark" to (R.drawable.ic_type_dark to R.color.color_type_dark),
            "fairy" to (R.drawable.ic_type_fairy to R.color.color_type_fairy)
        )
    }

    @Provides
    fun providePokemonService(retrofit: Retrofit): PokemonService {
        return retrofit.create(PokemonService::class.java)
    }

    @Provides
    fun provideRepository(
        service: PokemonService,
        elementSource: Map<String, Pair<Int, Int>>,
        pokemonDb: PokemonDB
    ): Repository {
        return RepositoryImpl(service, elementSource, pokemonDb.dao)
    }
}
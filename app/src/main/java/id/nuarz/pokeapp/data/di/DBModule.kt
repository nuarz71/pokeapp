package id.nuarz.pokeapp.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import id.nuarz.pokeapp.data.PokemonDB

@Module
@InstallIn(SingletonComponent::class)
object DBModule {

    @Provides
    fun providePokemonDatabase(@ApplicationContext context: Context): PokemonDB {
        return PokemonDB.create(context)
    }
}
package com.marcel.eatbymark.places

import com.marcel.eatbymark.core.AppConfig
import com.marcel.eatbymark.core.EATByMarkDatabase
import com.marcel.eatbymark.places.currentlocation.data.CurrentLocationDataSource
import com.marcel.eatbymark.places.currentlocation.data.DefaultCurrentLocationDataSource
import com.marcel.eatbymark.places.favouriteplaces.data.DefaultFavouritePlacesRepository
import com.marcel.eatbymark.places.favouriteplaces.data.FavouritePlacesDAO
import com.marcel.eatbymark.places.favouriteplaces.data.FavouritePlacesDataSource
import com.marcel.eatbymark.places.favouriteplaces.data.FavouritePlacesLocalDataSource
import com.marcel.eatbymark.places.favouriteplaces.domain.FavouritePlacesRepository
import com.marcel.eatbymark.places.placesaround.PlacesAroundConfig
import com.marcel.eatbymark.places.placesaround.data.DefaultPlacesAroundRepository
import com.marcel.eatbymark.places.placesaround.data.PlacesAroundAPI
import com.marcel.eatbymark.places.placesaround.data.PlacesAroundDataSource
import com.marcel.eatbymark.places.placesaround.data.PlacesAroundRemoteDataSource
import com.marcel.eatbymark.places.placesaround.domain.PlacesAroundRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PlacesModule {

    @Binds
    @Singleton
    abstract fun bindCurrentLocationDataSource(
        defaultCurrentLocationDataSource: DefaultCurrentLocationDataSource
    ): CurrentLocationDataSource

    @Binds
    @Singleton
    abstract fun bindFavouritePlacesDataSource(
        favouritePlacesLocalDataSource: FavouritePlacesLocalDataSource
    ): FavouritePlacesDataSource

    @Binds
    @Singleton
    abstract fun bindFavouritePlacesRepository(
        defaultFavouritePlacesRepository: DefaultFavouritePlacesRepository
    ): FavouritePlacesRepository

    @Binds
    @Singleton
    abstract fun bindPlacesAroundDataSource(
        placesAroundRemoteDataSource: PlacesAroundRemoteDataSource
    ): PlacesAroundDataSource

    @Binds
    @Singleton
    abstract fun bindPlacesAroundRepository(
        defaultPlacesAroundRepository: DefaultPlacesAroundRepository
    ): PlacesAroundRepository

    companion object {

        @Provides
        fun providePlacesAroundConfig(
            appConfig: AppConfig
        ): PlacesAroundConfig {
            return appConfig.placesAroundConfig
        }
    }

}

@Module
@InstallIn(SingletonComponent::class)
object FavouritePlacesDataAccessModule {
    @Provides
    fun provideFavouritePlacesDAO(
        database: EATByMarkDatabase
    ): FavouritePlacesDAO {
        return database.favouritePlacesDAO()
    }

    @Provides
    fun providePlacesAroundAPI(
        retrofit: Retrofit
    ): PlacesAroundAPI {
        return retrofit.create(PlacesAroundAPI::class.java)
    }
}
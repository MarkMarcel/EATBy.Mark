package com.marcel.eatbymark.core

import android.content.Context
import androidx.room.Room
import com.marcel.eatbymark.places.placesaround.PlacesAroundConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Qualifier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApplicationScope

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BackgroundDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IODispatcher

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {
    @Provides
    @Singleton
    @BackgroundDispatcher
    fun provideBackgroundDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @Singleton
    @IODispatcher
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    @ApplicationScope
    fun provideApplicationScope(
        @BackgroundDispatcher dispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(SupervisorJob() + dispatcher)
}

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {
    @Provides
    @Singleton
    fun provideAppConfig(): AppConfig {
        return AppConfig(
            databaseConfig = DatabaseConfig(
                databaseName = "eat-by-mark-database"
            ),
            networkClientConfig = NetworkClientConfig(
                baseUrl = "https://restaurant-api.wolt.com/v1/pages/",
                connectTimeoutSeconds = 3,
                readTimeoutSeconds = 5
            ),
            placesAroundConfig = PlacesAroundConfig(
                updatesIntervalSeconds = 10
            )
        )
    }

    @Provides
    fun provideDatabase(
        appConfig: AppConfig,
        @ApplicationContext context: Context
    ): EATByMarkDatabase {
        return Room.databaseBuilder(
            context,
            EATByMarkDatabase::class.java,
            appConfig.databaseConfig.databaseName,
        ).build()
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.HEADERS // Logs headers
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        appConfig: AppConfig,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(appConfig.networkClientConfig.connectTimeoutSeconds, TimeUnit.SECONDS)
            .readTimeout(appConfig.networkClientConfig.readTimeoutSeconds, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshiJsonConverter(): Converter.Factory {
        return MoshiConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        appConfig: AppConfig,
        okHttpClient: OkHttpClient,
        moshiJsonConverter: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(appConfig.networkClientConfig.baseUrl)
            .client(okHttpClient)
            .addConverterFactory(moshiJsonConverter)
            .build()
    }
}
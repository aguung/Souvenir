package com.devfutech.souvenir.di

import android.app.Application
import androidx.room.Room
import com.devfutech.souvenir.BuildConfig
import com.devfutech.souvenir.data.local.SouvenirDatabase
import com.devfutech.souvenir.data.network.SouvenirApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideDictionaryApi(retrofit: Retrofit): SouvenirApi =
        retrofit.create(SouvenirApi::class.java)

    @Provides
    @Singleton
    fun provideDatabase(
        app: Application
    ) = Room.databaseBuilder(app, SouvenirDatabase::class.java, "souvenir_database")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    fun provideDictionaryDao(db: SouvenirDatabase) = db.souvenirDao()
}



package com.devfutech.souvenir.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.devfutech.souvenir.BuildConfig
import com.devfutech.souvenir.data.local.SouvenirDatabase
import com.devfutech.souvenir.data.network.SouvenirApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
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

    @Singleton
    @Provides
    fun provideOkHttpClient(chuckerInterceptor: ChuckerInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(chuckerInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            })
            .build()
    }

    @Singleton
    @Provides
    fun provideChuckerInterceptor(
        @ApplicationContext context: Context,
        chuckerCollector: ChuckerCollector
    ): ChuckerInterceptor {
        return ChuckerInterceptor.Builder(context)
            .collector(chuckerCollector)
            .maxContentLength(250_000L)
            .redactHeaders("Auth-Token", "Bearer")
            .alwaysReadResponseBody(true)
            .build()
    }

    @Singleton
    @Provides
    fun provideChuckerCollector(@ApplicationContext context: Context): ChuckerCollector {
        return ChuckerCollector(
            context = context,
            showNotification = true,
            retentionPeriod = RetentionManager.Period.ONE_HOUR
        )
    }
}



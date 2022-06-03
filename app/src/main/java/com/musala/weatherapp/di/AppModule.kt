package com.musala.weatherapp.di

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import com.google.gson.Gson
import com.musala.weatherapp.util.Constants
import com.musala.weatherapp.util.SharedPreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideContext(@ApplicationContext context: Context) = context

    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun providesSharedPreferencesManager(sharedPreferences: SharedPreferences) =
        SharedPreferencesManager(sharedPreferences)

    @Provides
    @Singleton
    fun provideResources(context: Context) = context.resources
}
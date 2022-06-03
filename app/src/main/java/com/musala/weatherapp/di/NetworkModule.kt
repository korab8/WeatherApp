package com.musala.weatherapp.di

import android.util.Base64
import com.musala.weatherapp.network.APIEndpoints
import com.musala.weatherapp.network.helper.ApiHelper
import com.musala.weatherapp.network.helper.ApiHelperImpl
import com.musala.weatherapp.util.PublicData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpLogginInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        return logging
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(logging: HttpLoggingInterceptor): OkHttpClient {

        val httpClient = OkHttpClient.Builder()
        httpClient.readTimeout(4 * 60, TimeUnit.SECONDS)
        httpClient.connectTimeout(5, TimeUnit.SECONDS)
        // add logging as last interceptor
        httpClient.addInterceptor(logging)

        return httpClient.build()
    }


    @Provides
    @Singleton
    fun provideAPIEndpoints(httpClient: OkHttpClient): APIEndpoints {

        return Retrofit.Builder()
            .baseUrl(PublicData.ServerInfo.BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
            .create(APIEndpoints::class.java)

    }

    @Provides
    @Singleton
    fun provideApiHelper(apiHelper: ApiHelperImpl): ApiHelper = apiHelper
}

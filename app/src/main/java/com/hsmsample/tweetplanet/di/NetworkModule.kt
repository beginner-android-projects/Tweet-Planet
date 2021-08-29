package com.hsmsample.tweetplanet.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hsmsample.tweetplanet.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideHttpCache(@ApplicationContext context: Context): okhttp3.Cache {
        val cacheSize = 1 * 1024
        return Cache(context.cacheDir, cacheSize.toLong())
    }

    @Provides
    @Singleton
    fun provideHttpInterceptor(): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val originalRequest = chain.request()

            val builder = originalRequest.newBuilder().apply {
                method(originalRequest.method, originalRequest.body)
                addHeader(AUTHORIZATION, "$BEARER ${BuildConfig.BEARER_TOKEN}")
            }

            chain.proceed(builder.build())
        }
    }


    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.apply {
            level = if (!BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.NONE else HttpLoggingInterceptor.Level.BODY
        }

        return loggingInterceptor
    }


    @Provides
    @Singleton
    fun provideOkHttpClient(
        cache: okhttp3.Cache?,
        loggingInterceptor: HttpLoggingInterceptor?,
        interceptor: Interceptor?
    ): OkHttpClient {
        return OkHttpClient().newBuilder().apply {

            connectTimeout(
                TIMEOUT.toLong(),
                TimeUnit.SECONDS
            )

            readTimeout(
                TIMEOUT.toLong(),
                TimeUnit.SECONDS
            )

            writeTimeout(
                TIMEOUT.toLong(),
                TimeUnit.SECONDS
            )

            cache(cache)

            interceptor?.let { addInterceptor(it) }
//            loggingInterceptor?.let { addInterceptor(loggingInterceptor) }

        }.build()

    }


    @Provides
    @Singleton
    fun provideRetrofit(
        gson: Gson?,
        okHttpClient: OkHttpClient?
    ): Retrofit = Retrofit.Builder().apply {
        gson?.let { addConverterFactory(GsonConverterFactory.create(gson)) }
        baseUrl(BuildConfig.BASE_URL)
        okHttpClient?.let { client(okHttpClient) }
    }.build()


    companion object {
        private const val TIMEOUT = 90
        private const val BEARER = "bearer"
        private const val AUTHORIZATION = "Authorization"
    }

}
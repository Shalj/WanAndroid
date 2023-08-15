package com.shalj.wanandroid.di

import android.content.Context
import com.shalj.wanandroid.model.LoginResp
import com.shalj.wanandroid.net.Api
import com.shalj.wanandroid.net.ApiService
import com.shalj.wanandroid.net.toModel
import com.shalj.wanandroid.utils.logE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext context: Context) = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(HeaderInterceptor(context))
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://www.wanandroid.com")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit) = Api(retrofit.create(ApiService::class.java))

}

val loggingInterceptor = HttpLoggingInterceptor(
    object : HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
            logE("NetworkModule", "message:$message")
        }
    }
).apply { level = HttpLoggingInterceptor.Level.BODY }

class HeaderInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val userInfo = runBlocking {
            context.userDataStore.data.map { it[userInfoKey] }
                .firstOrNull()
                .orEmpty()
                .ifEmpty { "{}" }
                .toModel<LoginResp>()
        }
        return chain.proceed(
            chain.request().newBuilder()
                .addHeader("Cookie", "loginUserName=${userInfo.username}")
                .addHeader("Cookie", "loginUserPassword=${userInfo.password}")
                .build()
        )
    }
}
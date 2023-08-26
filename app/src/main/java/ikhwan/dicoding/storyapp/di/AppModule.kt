package ikhwan.dicoding.storyapp.di

import ikhwan.dicoding.storyapp.network.ApiService
import ikhwan.dicoding.storyapp.ui.add_story.AddStoryViewModel
import ikhwan.dicoding.storyapp.ui.detail.DetailViewModel
import ikhwan.dicoding.storyapp.ui.home.HomeViewModel
import ikhwan.dicoding.storyapp.ui.login.LoginViewModel
import ikhwan.dicoding.storyapp.ui.maps.MapsViewModel
import ikhwan.dicoding.storyapp.ui.register.RegisterViewModel
import ikhwan.dicoding.storyapp.utils.AuthRepository
import ikhwan.dicoding.storyapp.utils.StoryRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://story-api.dicoding.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val viewModelModule = module {
    viewModel { AddStoryViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { DetailViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { MapsViewModel(get()) }
}

val repositoryModule = module {
    single {
        StoryRepository(get())
    }
    single{
        AuthRepository(get())
    }
}
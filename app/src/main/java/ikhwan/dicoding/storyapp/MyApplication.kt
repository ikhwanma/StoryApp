package ikhwan.dicoding.storyapp

import android.app.Application
import ikhwan.dicoding.storyapp.di.networkModule
import ikhwan.dicoding.storyapp.di.repositoryModule
import ikhwan.dicoding.storyapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(
                listOf(
                    networkModule,
                    viewModelModule,
                    repositoryModule
                )
            )
        }
    }
}
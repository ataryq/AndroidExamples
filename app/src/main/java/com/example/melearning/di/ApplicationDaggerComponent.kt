package com.example.melearning.di

import android.app.Application
import android.content.Context
import com.example.melearning.CalculationHistoryDb
import com.example.melearning.DataProvider
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.hilt.migration.DisableInstallInCheck
import javax.inject.Singleton

@Module
@DisableInstallInCheck
class ApplicationModule(private val appContext: Context) {
    @Provides
    fun provideContext() = appContext
}

@Singleton
@Component(modules = [ApplicationModule::class, CalculationHistoryDb::class])
interface ApplicationComponent {
    fun dataProvider(): DataProvider
}

class ApplicationDaggerComponent: Application() {
    companion object {
        lateinit var instance: ApplicationDaggerComponent
    }

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        build()
    }

    private fun build() {
        applicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }
}
package com.example.melearning

import android.app.Application
import android.content.Context
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val appContext: Context) {
    @Provides
    fun provideContext() = appContext
}

@Singleton
@Component(modules = [ApplicationModule::class, CalculationDbModule::class])
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
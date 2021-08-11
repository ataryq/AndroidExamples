package com.example.melearning

import dagger.Component
import javax.inject.Scope

@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope

@ActivityScope
@Component(dependencies = [ApplicationComponent::class],
    modules = [MainActivity::class])
interface ActivityComponent {
    fun inject(obj: BottomNavigationFragment)
    fun inject(obj: CalculationFragment)
}

class ActivityDaggerComponent {
    companion object {
        val instance = ActivityDaggerComponent()
    }

    private lateinit var activityComponent: ActivityComponent

    fun inject(obj: CalculationFragment) = activityComponent.inject(obj)

    fun build(activity: MainActivity) {
        activityComponent = DaggerActivityComponent.builder()
            .applicationComponent(ApplicationDaggerComponent.instance.applicationComponent)
            .mainActivity(activity)
            .build()
    }
}


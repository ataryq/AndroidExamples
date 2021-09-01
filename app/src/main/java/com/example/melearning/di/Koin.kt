package com.example.melearning.di

import androidx.lifecycle.ViewModel
import com.example.melearning.fragments.main_activity.MainActivity
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Logger
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject

fun startKoinTests(activity: MainActivity) {
    startKoin {
        modules(appModule, myViewModels)
    }
    println(KoinExample().firstPresenter.sayHello())
    KoinExample().firstPresenter.name = "Igor"
    println(KoinExample().firstPresenter.sayHello())

    println("activity: ${activity.myKoinViewModel.sayHello()}")
}

val appModule = module {
    // single instance of HelloRepository
    factory <HelloRepository> { HelloRepositoryImpl() }

    // Simple Presenter Factory
    single { MySimplePresenter(get()) }
}

val myViewModels = module {
    viewModel {
        MyViewModel(get())
    }
}

class MyViewModel(private val repo: HelloRepository) : ViewModel() {
    fun sayHello() = "${repo.giveHello()} from viewModel"
}

//Example classes

class KoinExample {
    val firstPresenter: MySimplePresenter by inject(MySimplePresenter::class.java)
}

interface HelloRepository {
    fun giveHello(): String
}

class HelloRepositoryImpl : HelloRepository {
    var helloPhrase = "Hello Koin"
    override fun giveHello() = helloPhrase
}

class MySimplePresenter(private val repo: HelloRepository, var name: String = "???") {
    fun sayHello() = "${repo.giveHello()} from $name"
}
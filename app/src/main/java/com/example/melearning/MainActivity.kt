package com.example.melearning

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.melearning.ui_utils.KeyboardController
import dagger.Module
import dagger.Provides

@Module
class MainActivity : AppCompatActivity() {

    @Provides
    fun providerActivity() = this as AppCompatActivity
    @Provides
    fun providerContext(): Context = applicationContext

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ActivityDaggerComponent.instance.build(this)
        KeyboardController.instance.initialize(this)

        setCalculateFragment(savedInstanceState)
        println("onCreate:MainActivity")
    }

    private fun setCalculateFragment(savedInstanceState: Bundle?)
    {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.fragment_container_view, CalculationFragment(), CalculationFragment::class.java.name)
        }
    }
}
package com.example.melearning

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
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
        setCalculateFragment(savedInstanceState)
        println("onCreate:MainActivity")
    }

    private fun setCalculateFragment(savedInstanceState: Bundle?)
    {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<CalculationFragment>(R.id.fragment_container_view)
        }
    }
}
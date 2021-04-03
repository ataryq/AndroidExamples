package com.example.melearning

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ActivityDaggerComponent.instance.build(this)
        setCalculateFragment(savedInstanceState)
    }

    private fun setCalculateFragment(savedInstanceState: Bundle?)
    {
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<CalculationFragment>(R.id.fragment_container_view)
            }
        }
    }

}
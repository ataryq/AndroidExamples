package com.example.melearning

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit

class MainActivity : AppCompatActivity() {

    companion object {
        var instance: MainActivity? = null
        var CachedObjects = HashMap<String, Any>()

        fun getCachedDb(context: Context): CalculationHistoryDb {
            var db:CalculationHistoryDb
            if(CachedObjects.containsKey("CalculationHistoryDb"))
                db = CachedObjects["CalculationHistoryDb"] as CalculationHistoryDb
            else {
                db = CalculationHistoryDb(context)
                CachedObjects["CalculationHistoryDb"] = db
            }

            return db
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        instance = this

        getCachedDb(this)
        setCalculateFragment(savedInstanceState)
    }

    private fun setCalculateFragment(savedInstanceState: Bundle?)
    {
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<PercentCalculationFragment>(R.id.fragment_container_view)
            }
        }
    }

}
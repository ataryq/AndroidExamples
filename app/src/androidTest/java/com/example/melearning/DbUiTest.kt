package com.example.melearning

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.melearning.fragments.main_activity.MainActivity
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DbUiTest {
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db: CalculationHistoryDb.CalculationHistoryDatabase = Room.inMemoryDatabaseBuilder(context,
            CalculationHistoryDb.CalculationHistoryDatabase::class.java).build()
        val dao = db.historyDao()
    }

}
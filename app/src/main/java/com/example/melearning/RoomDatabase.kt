package com.example.melearning

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

interface DataProvider {
    fun getAll(): LiveData<List<CalculationHistoryDb.CalculationInfo>>
    fun insertAll(vararg histories: CalculationHistoryDb.CalculationInfo)
    fun delete(history: CalculationHistoryDb.CalculationInfo)
    fun clear()
}

@Module
class CalculationHistoryDb(context:Context, dbName: String = DbName): DataProvider {
    companion object {
        const val HistoryTableName = "CalculationHistory"
        const val DbName = "PercentCalculator"
        @Provides
        @Singleton
        fun createInstance(context:Context): DataProvider = CalculationHistoryDb(context)
    }

    private var mDb:CalculationHistoryDatabase
    private lateinit var mAllElements : LiveData<List<CalculationInfo>>
    private val mCoroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    init {
        mDb = Room.databaseBuilder(context,
            CalculationHistoryDatabase::class.java,
            dbName).build()
        updateAll()
    }

    private fun getDatabaseHandler(): CalculationHistoryDao {
        return mDb.historyDao()
    }

    @Entity(tableName = HistoryTableName)
    data class CalculationInfo(
        @ColumnInfo(name = "percent") val percent: Double,
        @ColumnInfo(name = "periods") val periods: Double,
        @ColumnInfo(name = "initial") val initial: Double,
        @ColumnInfo(name = "income") val income: Double,
        @PrimaryKey(autoGenerate = true) val id: Int? = null
        )

    @Dao
    interface CalculationHistoryDao {
        @Query("SELECT * FROM $HistoryTableName")
        fun getAll(): LiveData<List<CalculationInfo>>

        @Insert
        fun insertAll(vararg histories: CalculationInfo)

        @Delete
        fun delete(history: CalculationInfo)

        @Query("DELETE FROM $HistoryTableName")
        fun clearTable()
    }

    @Database(entities = [CalculationInfo::class], version = 1, exportSchema = false)
    abstract class CalculationHistoryDatabase : RoomDatabase() {
        abstract fun historyDao(): CalculationHistoryDao
    }

    override fun getAll() : LiveData<List<CalculationInfo>> {
        return mAllElements
    }

    private fun updateAll() {
        mAllElements = mDb.historyDao().getAll()
    }

    override fun insertAll(vararg histories: CalculationInfo) {
        mCoroutineScope.launch {
            getDatabaseHandler().insertAll(*histories)
            updateAll()
        }
    }

    override fun delete(history: CalculationInfo) {
        mCoroutineScope.launch {
            getDatabaseHandler().delete(history)
            updateAll()
        }
    }

    override fun clear() {
        mCoroutineScope.launch {
            mDb.clearAllTables()
            updateAll()
        }
    }
}
package com.example.melearning

import android.content.Context
import androidx.room.*
import dagger.*
import javax.inject.Singleton

@Module
class CalculationDbModule {
    @Provides
    @Singleton
    fun provideCalculationHistoryDb(context: Context): DataProvider = CalculationHistoryDb(context)
}

interface DataProvider {
    fun getAll(listener: LoadDataListener)
    fun insertAll(vararg histories: CalculationHistoryDb.CalculationInfo)
    fun delete(history: CalculationHistoryDb.CalculationInfo)
    fun clear()
    fun setDataChangedCallback(callback: Utils.Callback)
}

interface LoadDataListener {
    fun getData(data:List<CalculationHistoryDb.CalculationInfo>)
}

class CalculationHistoryDb(context:Context, dbName: String = DbName): DataProvider {
    companion object {
        const val HistoryTableName = "CalculationHistory"
        const val DbName = "PercentCalculator"
    }

    private var mDb:CalculationHistoryDatabase
    fun getDatabase(): CalculationHistoryDatabase { return mDb }

    private var mDataChangedCallback: Utils.Callback? = null

    init {
        mDb = Room.databaseBuilder(context,
            CalculationHistoryDatabase::class.java,
            dbName).build()
    }

    private fun getDatabaseHandler(): CalculationHistoryDao {
        return mDb.historyDao()
    }

    @Entity(tableName = HistoryTableName)
    data class CalculationInfo(
        @ColumnInfo(name = "percent") val percent: Double?,
        @ColumnInfo(name = "periods") val periods: Double?,
        @ColumnInfo(name = "initial") val initial: Double?,
        @ColumnInfo(name = "income") val income: Double?,
        @PrimaryKey(autoGenerate = true) val id: Int? = null
        )

    @Dao
    interface CalculationHistoryDao {
        @Query("SELECT * FROM $HistoryTableName")
        fun getAll(): List<CalculationInfo>

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

    override fun getAll(listener: LoadDataListener){
        Utils.runInThread {
            println("getAll")
            var data = getDatabaseHandler().getAll()
            Utils.runInUiThread {
                println("getAll in ui")
                listener.getData(data)
            }
        }
    }

    override fun insertAll(vararg histories: CalculationInfo) {
        Utils.runInThread {
            getDatabaseHandler().insertAll(*histories)
            Utils.runInUiThread { mDataChangedCallback?.called() }
        }
    }

    override fun delete(history: CalculationInfo) {
        Utils.runInThread {
            getDatabaseHandler().delete(history)
            Utils.runInUiThread { mDataChangedCallback?.called() }
        }
    }

    override fun clear() {
        Utils.runInThread {
            mDb.clearAllTables()
        }
    }

    override fun setDataChangedCallback(callback: Utils.Callback) {
        mDataChangedCallback = callback
    }
}
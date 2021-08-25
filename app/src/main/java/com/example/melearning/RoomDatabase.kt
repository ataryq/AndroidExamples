package com.example.melearning

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import dagger.Module
import dagger.Provides
import io.reactivex.Flowable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

interface DataProvider {
    fun getAll(): LiveData<List<CalculationHistoryDb.CalculationInfo>>
    fun getAllRx(): Flowable<List<CalculationHistoryDb.CalculationInfo>>
    fun insertAll(vararg histories: CalculationHistoryDb.CalculationInfo)
    fun update(vararg histories: CalculationHistoryDb.CalculationInfo)
    fun delete(history: CalculationHistoryDb.CalculationInfo)
    fun clear()
    fun updateDbInProgress(): Boolean
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
    private val mCoroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private val mOperationInProgress = MutableLiveData(false)

    init {
        mDb = Room.databaseBuilder(context,
            CalculationHistoryDatabase::class.java,
            dbName).build()
        getDatabaseHandler().getAll().observeForever {
            mOperationInProgress.postValue(false)
        }
    }

    private fun getDatabaseHandler(): CalculationHistoryDao {
        return mDb.historyDao()
    }

    @Entity(tableName = HistoryTableName)
    data class CalculationInfo(
        @ColumnInfo(name = "percent") var percent: Double,
        @ColumnInfo(name = "periods") var periods: Double,
        @ColumnInfo(name = "initial") var initial: Double,
        @ColumnInfo(name = "income") var income: Double,
        @PrimaryKey(autoGenerate = true) val id: Int? = null
        )

    @Dao
    interface CalculationHistoryDao {
        @Query("SELECT * FROM $HistoryTableName")
        fun getAll(): LiveData<List<CalculationInfo>>

        @Query("SELECT * FROM $HistoryTableName")
        fun getAllRx(): Flowable<List<CalculationInfo>>

        @Insert
        fun insertAll(vararg histories: CalculationInfo)

        @Delete
        fun delete(history: CalculationInfo)

        @Update
        fun update(vararg histories: CalculationInfo)

        @Query("DELETE FROM $HistoryTableName")
        fun clearTable()
    }

    @Database(entities = [CalculationInfo::class], version = 1, exportSchema = false)
    abstract class CalculationHistoryDatabase : RoomDatabase() {
        abstract fun historyDao(): CalculationHistoryDao
    }

    override fun getAll() = mDb.historyDao().getAll()
    override fun getAllRx() = mDb.historyDao().getAllRx()

    override fun insertAll(vararg histories: CalculationInfo) {
        mOperationInProgress.postValue(true)
        mCoroutineScope.launch {
            getDatabaseHandler().insertAll(*histories)
        }
    }

    override fun update(vararg histories: CalculationInfo) {
        mOperationInProgress.postValue(true)
        mCoroutineScope.launch {
            getDatabaseHandler().update(*histories)
        }
    }

    override fun delete(history: CalculationInfo) {
        mOperationInProgress.postValue(true)
        mCoroutineScope.launch {
            getDatabaseHandler().delete(history)
        }
    }

    override fun clear() {
        mOperationInProgress.postValue(true)
        mCoroutineScope.launch {
            mDb.clearAllTables()
        }
    }

    override fun updateDbInProgress() = mOperationInProgress.value!!
}
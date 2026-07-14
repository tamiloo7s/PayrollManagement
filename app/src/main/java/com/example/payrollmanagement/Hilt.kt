package com.example.payrollmanagement

import android.content.Context
import androidx.room.Room
import com.example.payrollmanagement.data.AppDatabase
import com.example.payrollmanagement.data.PayrollDao
import com.example.payrollmanagement.data.repository.PayrollRepositoryImpl
import com.example.payrollmanagement.domain.repository.PayrollRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "payroll_database"
        ).allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun providePayrollDao(db: AppDatabase): PayrollDao{
        return db.payrollDao()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindPayrollRepository(
        impl: PayrollRepositoryImpl
    ): PayrollRepository
}
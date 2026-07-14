package com.example.payrollmanagement.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [payrollEntity::class, EmployeeEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun payrollDao(): PayrollDao

}
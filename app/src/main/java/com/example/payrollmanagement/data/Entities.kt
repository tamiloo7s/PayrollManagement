package com.example.payrollmanagement.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "payrolls")
data class payrollEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0,
    val creationDate:Long
)


@Entity(
    tableName = "employees",
    foreignKeys = [
        ForeignKey(
            entity = payrollEntity::class,
            parentColumns = ["id"],
            childColumns = ["payrollId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["payrollId"])]
)
data class EmployeeEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0,
    val payrollId:Long,
    val name:String,
    val wages:Double,
    val isExempt: Boolean
)

data class payrollWithEmployee(
    @Embedded val payroll: payrollEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "payrollId"
    )
    val employees:List<EmployeeEntity>
)
package com.example.proyectoalcaravan.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserDB(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?,
    @ColumnInfo(name = "birthday") val birthday: String?,
    @ColumnInfo(name = "cedula") val cedula: Int?,
    @ColumnInfo(name = "gender") val gender: String?,
    @ColumnInfo(name = "imageProfile") val imageProfile: String?,
    @ColumnInfo(name = "email") val email: String?,
    @ColumnInfo(name = "password") val password: String?,
    @ColumnInfo(name = "rol") val rol: String?,
    @ColumnInfo(name = "phone") val phone: Long?,
    @ColumnInfo(name = "lgn") val lgn: Double?,
    @ColumnInfo(name = "lag") val lag: Double?,
//    @ColumnInfo(name = "listActivities") val listActivities: List<Actividad>,
    // Add other columns as per your requirements
)

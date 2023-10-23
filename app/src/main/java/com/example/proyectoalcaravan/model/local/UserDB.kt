package com.example.proyectoalcaravan.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.proyectoalcaravan.model.remote.Actividad

@Entity(tableName = "users")
data class UserDB(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?,
    @ColumnInfo(name = "birthday") val birthday: String?,
    @ColumnInfo(name = "cedula") val cedula: Number?,
    @ColumnInfo(name = "gender") val gender: String?,
    @ColumnInfo(name = "imageProfile") val imageProfile: String?,
    @ColumnInfo(name = "email") val email: String?,
    @ColumnInfo(name = "password") val password: String?,
    @ColumnInfo(name = "rol") val rol: String?,
    @ColumnInfo(name = "phone") val phone: Number?,
    @ColumnInfo(name = "lgn") val lgn: String?,
    @ColumnInfo(name = "lag") val lag: String?,
    @ColumnInfo(name = "listActivities") val listActivities: Any,
    // Add other columns as per your requirements
)

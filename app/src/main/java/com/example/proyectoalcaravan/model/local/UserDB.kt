package com.example.proyectoalcaravan.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.proyectoalcaravan.model.converters.Converters
import com.example.proyectoalcaravan.model.remote.Actividad
import com.example.proyectoalcaravan.model.remote.Materia

@Entity(tableName = "users")
@TypeConverters(Converters::class)
data class UserDB(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "user_id") val userId: Int?,
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
    @ColumnInfo(name = "listActivities") val listActivities: List<Actividad?>?,
    @ColumnInfo(name = "listOfMaterias") val listOfMaterias: List<Materia>?,

    // Add other columns as per your requirements
)

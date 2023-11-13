package com.example.proyectoalcaravan.model.converters

import androidx.room.TypeConverter
import com.example.proyectoalcaravan.model.remote.Actividad
import com.example.proyectoalcaravan.model.remote.Materia
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun fromString(value: String): List<Actividad> {
        val listType = object : TypeToken<List<Actividad>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromListMateria(list: List<Materia>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromStringMateria(value: String): List<Materia> {
        val listType = object : TypeToken<List<Materia>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromListActividad(list: List<Actividad?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}

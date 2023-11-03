package com.example.proyectoalcaravan.repository

import com.example.proyectoalcaravan.model.local.UserDB
import com.example.proyectoalcaravan.model.local.UserDao
import com.example.proyectoalcaravan.model.remote.Actividad
import com.example.proyectoalcaravan.model.remote.Materia
import com.example.proyectoalcaravan.model.remote.RetrofitService
import com.example.proyectoalcaravan.model.remote.User
import retrofit2.Call

class MainRepository(private val retrofitService: RetrofitService, val userDao: UserDao) {

    //Usuarios Retrofit
    fun getAllUsers() = retrofitService.getAllUsers()
    fun getUserStudentsByFirstName(name: String): Call<List<User>> = retrofitService.getUserStudentsByFirstName(name)
    fun getUserStudentsByEmail(email: String): Call<List<User>> = retrofitService.getUserStudentsByEmail(email)
    fun getUserStudentsByGender(gender: String): Call<List<User>> = retrofitService.getUserStudentsByGender(gender)

    fun getUserStudents(rol: String): Call<List<User>> = retrofitService.getUserStudents(rol)

    fun createUser(user: User): Call<User> = retrofitService.createUser(user)

    fun updateUser(userId: Int, user: User): Call<User> = retrofitService.updateUser(userId, user)

    fun deleteUser(userId: Int): Call<Void> = retrofitService.deleteUser(userId)

    fun getUserById(userId: Int): Call<User> = retrofitService.getUserById(userId)

    //Clases retrofit
    fun getAllMaterias() = retrofitService.getAllMaterias()

    fun getMateriaById(materiaId: Int) = retrofitService.getClassById(materiaId)
    fun updateMateria(materiaId: Int, materia: Materia) = retrofitService.updateMateria(materiaId, materia)

    //Actividades
    fun getAllActivities() = retrofitService.getAllActivities()
    fun getActivityByIdClass(activityId: Int) = retrofitService.getActivityByIdClass(activityId)
    fun createActivity(actividad: Actividad): Call<Actividad> = retrofitService.createActivity(actividad)
    fun updateActivity(actividadId: Int, actividad: Actividad): Call<Actividad> = retrofitService.updateActivity(actividadId, actividad)
    fun deleteActivity(actividadId: Int): Call<Void> = retrofitService.deleteActivity(actividadId)


    //Room

//    suspend fun getAllUsersDB() = userDao.getAllUsers()

    suspend fun createUserDB(user: UserDB) = userDao.insertUser(user)

//    suspend fun updateUserDB(user: User) = userDao.updateUser(user)

//    suspend fun deleteUserDB(userId: Int) = userDao.deleteUser(userId)

    suspend fun getUserByIdDB(userId: Int) = userDao.getUserById(userId)



}
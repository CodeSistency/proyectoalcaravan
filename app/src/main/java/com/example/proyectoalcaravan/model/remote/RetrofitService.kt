package com.example.proyectoalcaravan.model.remote

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitService {
    @GET("users")
    fun getAllUsers(): Call<List<User>>
    @GET("users")
    fun getUserStudents(@Query("rol") rol: String): Call<List<User>>

    @GET("users")
    fun getUserStudentCedula(@Query("cedula") cedula: Int): Call<List<User>>

    @GET("users")
    fun LoginRetrofit(@Query("email") email: String, @Query("password") password: String): Call<List<User>>

    @GET("users")
    fun getUserStudentsByFirstName(@Query("firstName") rol: String): Call<List<User>>
    @GET("users")
    fun getUserStudentsByGender(@Query("gender") gender: String): Call<List<User>>

    @GET("users")
    fun getUserStudentsByEmail(@Query("email") email: String): Call<List<User>>

    @POST("users")
    fun createUser(@Body user: User): Call<User>

    @PUT("users/{id}")
    fun updateUser(@Path("id") userId: Int, @Body user: User): Call<User>

    @DELETE("users/{id}")
    fun deleteUser(@Path("id") userId: Int): Call<Void>

    @GET("users/{id}")
    fun getUserById(@Path("id") userId: Int): Call<User>

    //Clases

    @GET("class")
    fun getAllMaterias(): Call<List<Materia>>

    @GET("class/{id}")
    fun getClassById(@Path("id") classId: Int): Call<Materia>

    @PUT("class/{id}")
    fun updateMateria(@Path("id") materiaId: Int, @Body materia: Materia): Call<Materia>

    //Actividades
    @GET("activities")
    fun getAllActivities(): Call<List<Actividad>>

    @GET("activities")
    fun getActivityByIdClass(@Query("idClass") actividadId: Int): Call<List<Actividad>>

    @POST("activities")
    fun createActivity(@Body actividad: Actividad): Call<Actividad>

    @PUT("activities/{id}")
    fun updateActivity(@Path("id") actividadId: Int, @Body actividad: Actividad): Call<Actividad>

    @DELETE("activities/{id}")
    fun deleteActivity(@Path("id") actividadId: Int): Call<Void>

    companion object {

        var retrofitService: RetrofitService? = null

        //Create the Retrofit service instance using the retrofit.
        fun getInstance(): RetrofitService {

            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://185.221.23.139/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }
    }
}
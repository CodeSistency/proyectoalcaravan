package com.example.proyectoalcaravan.dependency_injection

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyectoalcaravan.model.local.AppDatabase
import com.example.proyectoalcaravan.model.remote.RetrofitService
import com.example.proyectoalcaravan.repository.MainRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

//interface AppModule{
//    val retrofitService: RetrofitService
//    val repository: MainRepository
//    val dataBase: AppDatabase
//}
//class AppModuleImpl(
//    private val appContext: Context
//): AppModule{
//    override val retrofitService: RetrofitService by  lazy {
//       Retrofit.Builder()
//           .baseUrl("http://185.221.23.139/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create()
//    }
//    override val repository: MainRepository by lazy {
//            repository(retrofitService)
//    }
//
//
//}
//
//class viewModelFactory: ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return super.create(modelClass)
//    }
//}
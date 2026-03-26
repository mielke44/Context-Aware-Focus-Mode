package com.wilson.focusmode.core

import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.wilson.focusmode.core.api.RESTApi
import com.wilson.focusmode.core.database.AppDatabase
import com.wilson.focusmode.core.interfaces.DistractionSensor
import com.wilson.focusmode.core.interfaces.FocusServiceController
import com.wilson.focusmode.core.interfaces.FocusTimer
import com.wilson.focusmode.core.sensors.AccelerometerSensor
import com.wilson.focusmode.core.sensors.MicrophoneSensor
import com.wilson.focusmode.core.service.FocusForegroundService
import com.wilson.focusmode.core.utils.NotificationUtil
import com.wilson.focusmode.ui.viewmodel.BaseViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {

    single {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://api.mock-server.com/")
            .client(get<OkHttpClient>())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RESTApi::class.java)
    }

    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "app_database")
            .fallbackToDestructiveMigration(false)
            .build()
    }

    single { get<AppDatabase>().focusSessionDao() }

    factory { NetworkManager(get()) }
    single { FocusRepository(get()) }
    single { NotificationUtil(get()) }

    factory<DistractionSensor<Float>>(named("accelerometer")) { AccelerometerSensor(get()) }
    factory<DistractionSensor<Int>>(named("microphone")) { MicrophoneSensor(get()) }
    factory<FocusTimer> { SessionTimer() }

    viewModel {
        BaseViewModel(get(), get())
    }

    single<FocusServiceController> {
        object : FocusServiceController {
            private val context = androidContext()

            override fun startService() {
                val intent = Intent(context, FocusForegroundService::class.java).apply {
                    action = FocusForegroundService.ACTION_START
                }
                ContextCompat.startForegroundService(context, intent)
            }

            override fun stopService() {
                val intent = Intent(context, FocusForegroundService::class.java).apply {
                    action = FocusForegroundService.ACTION_STOP
                }
                context.stopService(intent)
            }
        }
    }
}
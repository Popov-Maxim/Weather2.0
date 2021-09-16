package com.mycompany.wind

import android.app.Application
import com.mycompany.wind.di.AppComponent
import com.mycompany.wind.di.AppModule
import com.mycompany.wind.di.DaggerAppComponent

class App : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder().appModule(AppModule(applicationContext)).build()
    }

}
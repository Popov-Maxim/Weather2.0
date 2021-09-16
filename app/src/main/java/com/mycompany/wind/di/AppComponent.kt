package com.mycompany.wind.di

import com.mycompany.wind.ui.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
abstract class AppComponent {
    abstract fun inject(mainActivity: MainActivity)
}
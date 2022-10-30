package com.example.digitalreceipts

import android.app.Application
import com.example.digitalreceipts.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Classe responsável pela criação das injeções de dependência usando Koin.
 */
class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Inicia a biblioteca
        startKoin {
            androidContext(this@AppApplication)
            //Referência ao AppModule.kt
            modules(viewModelModule)
        }
    }
}
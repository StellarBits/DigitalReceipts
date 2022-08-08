package com.example.digitalreceipts

import android.app.Application
import android.os.StrictMode
import com.example.digitalreceipts.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AppApplication)
            modules(viewModelModule)
        }

//        /**
//         * Um hack rápido para evitar a FileUriExposedException. Se fosse um app de produção
//         * deveria ser usada uma solução baseada em ContentProvider.
//         */
//        val builder = StrictMode.VmPolicy.Builder()
//        StrictMode.setVmPolicy(builder.build())
    }
}
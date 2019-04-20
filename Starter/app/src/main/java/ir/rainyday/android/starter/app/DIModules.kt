package com.app.example.app

import com.app.example.modules.sample.sampleModule
import org.koin.core.module.Module

object DIModules {
    val getModules: Iterable<Module>
        get() {
            return listOf(
                    appModule,
                    // add modules here
            )
        }
}
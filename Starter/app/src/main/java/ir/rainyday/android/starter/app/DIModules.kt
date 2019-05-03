package ir.rainyday.android.starter.app


import org.koin.core.module.Module

object DIModules {
    val getModules: Iterable<Module>
        get() {
            return listOf(
                    appModule
                    //, sampleModule
                    // add modules here
            )
        }
}
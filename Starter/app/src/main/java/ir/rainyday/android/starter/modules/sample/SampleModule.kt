package ir.rainyday.android.starter.modules.sample


import ir.rainyday.android.starter.app.AppDataBase
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val sampleModule = module {
    single { get<AppDataBase>().sampleDao() }
    single { get<Retrofit>().create(SampleApi::class.java) }
    single<SampleRepo> { SampleRepoImp(get(), get()) }
    viewModel { SampleViewModel(get()) }
}
package ${appPackage}.modules.${moduleNameLower}

<#if includeLocale>
import ${appPackage}.app.AppDataBase
</#if>
<#if includeWebApi>
import ${appPackage}.data.remote.${moduleName}Api
</#if>
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

//todo add module to DIModules
val ${moduleNameLower}Module = module {
    <#if includeLocale>
    single { get<AppDataBase>().${moduleNameLower}Dao() }
    </#if>
    <#if includeWebApi>
    single { get<Retrofit>().create(${moduleName}Api::class.java) }
    </#if>
     <#if includeLocale && includeWebApi>
    single<${moduleName}Repo> { ${moduleName}RepoImp(get(), get()) }
    <#elseif includeLocale || includeWebApi>
     single<${moduleName}Repo> { ${moduleName}RepoImp(get()) }
     <#else>
    single<${moduleName}Repo> { ${moduleName}RepoImp() }
    </#if>
    <#if includeLocale || includeWebApi>
   viewModel { ${moduleName}ViewModel(get()) }
    <#else>
   viewModel { ${moduleName}ViewModel() }
    </#if>
}

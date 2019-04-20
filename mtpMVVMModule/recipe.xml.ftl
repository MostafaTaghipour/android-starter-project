<?xml version="1.0"?>
<#import "root://activities/common/kotlin_macros.ftl" as kt>
<recipe>
    <@kt.addAllKotlinDependencies />

     <#if viewType == 'activity'>
    <instantiate from="root/src/app_package/activity.kt.ftl"
                   to="${escapeXmlAttribute(packageRootOut)}/modules/${moduleNameLower}/${moduleName}Activity.kt" />

                    
    <instantiate from="root/res/layout/activity_container.xml.ftl"
                    to="${escapeXmlAttribute(resOut)}/layout/activity_${moduleNameLower}.xml" />

     <instantiate from="root/res/layout/activity_content.xml.ftl"
                    to="${escapeXmlAttribute(resOut)}/layout/content_${moduleNameLower}.xml" />

    <open file="${escapeXmlAttribute(packageRootOut)}/modules/${moduleNameLower}/${moduleName}Activity.kt" />
    <#else>
        <instantiate from="root/src/app_package/fragment.kt.ftl"
                   to="${escapeXmlAttribute(packageRootOut)}/modules/${moduleNameLower}/${moduleName}Fragment.kt" />

                    
    <instantiate from="root/res/layout/fragment.xml.ftl"
                    to="${escapeXmlAttribute(resOut)}/layout/fragment_${moduleNameLower}.xml" />

    <open file="${escapeXmlAttribute(packageRootOut)}/modules/${moduleNameLower}/${moduleName}Fragment.kt" />
    </#if>

    <instantiate from="root/res/values/string.xml.ftl"
                    to="${escapeXmlAttribute(resOut)}/values/string_${moduleNameLower}.xml" />

    <instantiate from="root/res/values/string.xml.ftl"
                    to="${escapeXmlAttribute(resOut)}/values-fa/string_${moduleNameLower}.xml" />

  <merge from="root/AndroidManifest.xml.ftl"
             to="${escapeXmlAttribute(manifestOut)}/AndroidManifest.xml" />


<instantiate from="root/src/app_package/dto.kt.ftl"
                   to="${escapeXmlAttribute(packageRootOut)}/data/model/dto/${moduleName}DTO.kt" />

   <instantiate from="root/src/app_package/viewmodel.kt.ftl"
                  to="${escapeXmlAttribute(packageRootOut)}/modules/${moduleNameLower}/${moduleName}ViewModel.kt" />
                     
     <instantiate from="root/src/app_package/module.kt.ftl"
                  to="${escapeXmlAttribute(packageRootOut)}/modules/${moduleNameLower}/${moduleName}Module.kt" />
   <#if includeLocale>
    <instantiate from="root/src/app_package/dao.kt.ftl"
                   to="${escapeXmlAttribute(packageRootOut)}/data/local/${moduleName}Dao.kt" />
                       <instantiate from="root/src/app_package/entity.kt.ftl"
                   to="${escapeXmlAttribute(packageRootOut)}/data/model/entity/${moduleName}Entity.kt" />
   </#if>

                   
   <#if includeWebApi>
    <instantiate from="root/src/app_package/api.kt.ftl"
                   to="${escapeXmlAttribute(packageRootOut)}/data/remote/${moduleName}Api.kt" />
                       <instantiate from="root/src/app_package/json.kt.ftl"
                   to="${escapeXmlAttribute(packageRootOut)}/data/model/json/${moduleName}Json.kt" />
   </#if>


   <#if includeLocale || includeWebApi>
<instantiate from="root/src/app_package/transformer.kt.ftl"
                   to="${escapeXmlAttribute(packageRootOut)}/data/model/transformer/${moduleName}Transformer.kt" />
                   <instantiate from="root/src/app_package/repo.kt.ftl"
                      to="${escapeXmlAttribute(packageRootOut)}/modules/${moduleNameLower}/${moduleName}Repo.kt" />
    </#if>

    <#if includeBaseClasses>
    <instantiate from="root/src/app_package/BaseMvvmActivity.kt.ftl"
                   to="${escapeXmlAttribute(packageRootOut)}/modules/shared/base/BaseMvvmActivity.kt" />
     <instantiate from="root/src/app_package/BaseMvvmDialogFragment.kt.ftl"
                   to="${escapeXmlAttribute(packageRootOut)}/modules/shared/base/BaseMvvmDialogFragment.kt" />
    <instantiate from="root/src/app_package/BaseMvvmFragment.kt.ftl"
                   to="${escapeXmlAttribute(packageRootOut)}/modules/shared/base/BaseMvvmFragment.kt" />
     <instantiate from="root/src/app_package/BaseViewModel.kt.ftl"
                   to="${escapeXmlAttribute(packageRootOut)}/modules/shared/base/BaseViewModel.kt" />                
   </#if>

</recipe>

package ${appPackage}.data.model.transformer

import ${appPackage}.data.model.dto.${moduleName}DTO
<#if includeLocale>
import ${appPackage}.data.model.entity.${moduleName}Entity
</#if>
<#if includeWebApi>
import ${appPackage}.data.model.json.${moduleName}Json
</#if>

 <#if includeLocale>
fun ${moduleName}Entity.toDTO(): ${moduleName}DTO {
    return ${moduleName}DTO(id, title)
}

fun ${moduleName}DTO.toEntity(): ${moduleName}Entity {
    return ${moduleName}Entity(id, title)
}
</#if>

 <#if includeWebApi>
fun ${moduleName}DTO.toJson(): ${moduleName}Json {
    return ${moduleName}Json(id, title)
}

fun ${moduleName}Json.toDTO(): ${moduleName}DTO {
    return ${moduleName}DTO(id, title)
}
</#if>


<#if includeLocale && includeWebApi>
fun ${moduleName}Entity.toJson(): ${moduleName}Json {
    return ${moduleName}Json(id, title)
}

fun ${moduleName}Json.toEntity(): ${moduleName}Entity {
    return ${moduleName}Entity(id, title)
}
</#if>
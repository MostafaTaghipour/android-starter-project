<?xml version="1.0"?>
<globals>
    
     <#include "root://activities/common/common_globals.xml.ftl" />
     <#if applicationPackage??>
     <global id="appPackage" value='${applicationPackage?replace(".dev","")?replace(".stg","")}' />
     <global id="moduleNameLower" value='${moduleName?lower_case}' />
     <global id="packageRootOut" value='${manifestOut}/java/${slashedPackageName(applicationPackage?replace(".dev","")?replace(".stg",""))}' />
     </#if>
</globals>

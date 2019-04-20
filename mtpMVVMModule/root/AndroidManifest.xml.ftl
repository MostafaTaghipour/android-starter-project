<#import "root://activities/common/shared_manifest_macros.ftl" as manifestMacros>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="${appPackage}">

    <application>
     <#if viewType == 'activity'>
         <activity android:name=".modules.${moduleNameLower}.${moduleName}Activity" />
      </#if>
    </application>
</manifest>

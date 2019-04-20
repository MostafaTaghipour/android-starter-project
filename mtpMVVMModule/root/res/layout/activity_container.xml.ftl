<?xml version="1.0" encoding="utf-8"?>
<${getMaterialComponentName('android.support.design.widget.CoordinatorLayout', useAndroidX)}
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fitsSystemWindows="true"
    tools:context="${appPackage}.modules.${moduleNameLower}.${moduleName}Activity">

    <include layout="@layout/layout_regular_app_bar" />

    <include layout="@layout/content_${moduleNameLower}" />



</${getMaterialComponentName('android.support.design.widget.CoordinatorLayout', useAndroidX)}>

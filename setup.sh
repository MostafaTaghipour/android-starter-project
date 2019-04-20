#! /bin/bash
read -p "   <<<<<< Enter Your App Name: "  projectName
read -p "   <<<<<< Enter Your App PackageName/Id: "  packageName
read -p "   <<<<<< Enter Your App minSdkVersion (default is 17): "  minSDK
read -p "   <<<<<< Enter Your App Locale (fa/en, default is fa): "  locale

echo "   >>>>>> Initializing project... , please wait"


mv starter $projectName
cd $projectName
#mv starter.iml $projectName.iml

projectDir=$(pwd)
packagePath="${packageName//./$'/'}"

cd app/src/main/java
mkdir -p "$packagePath"
mv ir _i
cp -r _i/rainyday/android/starter/* "$packagePath"
rm -r _i

cd ../../androidTest/java
mkdir -p "$packagePath"
mv ir _i
cp -r _i/rainyday/android/starter/* "$packagePath"
rm -r _i

cd ../../test/java
mkdir -p "$packagePath"
mv ir _i
cp -r _i/rainyday/android/starter/* "$packagePath"
rm -r _i


cd "$projectDir"
find . -type f \( -name "*xml" -o -name "*.java" -o -name "*.gradle" -o -name "*.kt" \) -exec sed -i '' -e "s/ir.rainyday.android.starter/$packageName/g" {} \;


find . -name 'versions.gradle' -print0 | xargs -0 sed -i "" "s/17/$minSDK/g"

cd app
find . -name 'flavors.gradle' -print0 | xargs -0 sed -i "" "s/Starter/$projectName/g"


cd "src/main/java/$packagePath/app"
find . -name 'App.kt' -print0 | xargs -0 sed -i "" "s/Locale(\"fa\")/Locale(\"$locale\")/g"

cd "$projectDir"


echo "   >>>>>> Project initialized"
cd ../
echo "   >>>>>> Openning project... , please wait"
open -a /Applications/Android\ Studio.app "./$projectName"

cp -r mtpMVVMModule /Applications/Android\ Studio.app/Contents/plugins/android/lib/templates/other
echo "   >>>>>> 'mtpMVVMModule' moved to AndroidStudio templates"
rm -R mtpMVVMModule

rm -R screenshots
rm -R README.md


echo "   >>>>>> extra files removed"
rm -- "$0"

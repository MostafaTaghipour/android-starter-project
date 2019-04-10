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






#mv $projectName/starter $projectName/$projectName
#mv $projectName/starter.xcodeproj $projectName/$projectName.xcodeproj
#mv $projectName/starter.xcworkspace $projectName/$projectName.xcworkspace
#cd $projectName
#cd $projectName.xcworkspace
#find . -exec sed -i '' -e "s/starter/$projectName/g" {} \;
#cd ../
#cd $projectName.xcodeproj
#find . -exec sed -i '' -e "s/starter/$projectName/g" {} \;
#find . -name 'project.pbxproj' -print0 | xargs -0 sed -i "" "s/9.0/$deoployTarget/g"
#find . -name 'project.pbxproj' -print0 | xargs -0 sed -i "" "s/developmentRegion = fa;/developmentRegion = $devRegion;/g"
#mv xcuserdata/starter.xcuserdatad xcuserdata/$(id -un).xcuserdatad
#cd ../
#cd Common/Common.xcodeproj
#find . -exec sed -i '' -e "s/starter/$projectName/g" {} \;
#find . -name 'project.pbxproj' -print0 | xargs -0 sed -i "" "s/9.0/$deoployTarget/g"
#find . -name 'project.pbxproj' -print0 | xargs -0 sed -i "" "s/developmentRegion = fa;/developmentRegion = $devRegion;/g"
#cd ../../
#find . -name 'Podfile' -print0 | xargs -0 sed -i "" "s/starter/$projectName/g"
#cd $projectName
#cd 'app'
#find . -name 'Production.xcconfig' -print0 | xargs -0 sed -i "" "s/Starter/$projectName/g"
#find . -name 'Staging.xcconfig' -print0 | xargs -0 sed -i "" "s/Starter/$projectName/g"
#find . -name 'Development.xcconfig' -print0 | xargs -0 sed -i "" "s/Starter/$projectName/g"
#find . -name 'Production.xcconfig' -print0 | xargs -0 sed -i "" "s/ir.rainyday.ios.starter/$bundleId/g"
#find . -name 'Staging.xcconfig' -print0 | xargs -0 sed -i "" "s/ir.rainyday.ios.starter/$bundleId/g"
#find . -name 'Development.xcconfig' -print0 | xargs -0 sed -i "" "s/ir.rainyday.ios.starter/$bundleId/g"
echo "   >>>>>> Project initialized"
#cd ../../
#echo "   >>>>>> Installing dependencies... , please wait"
#pod install
#echo "   >>>>>> Dependencies installed"
cd ../
echo "   >>>>>> Openning project... , please wait"
open -a /Applications/Android\ Studio.app "./$projectName"
#open "$projectName.xcworkspace"
#cd ../
#mkdir -p ~/Library/Developer/Xcode/Templates/File\ Templates
#cp -r MTP\ Templates ~/Library/Developer/Xcode/Templates/File\ Templates
#echo "   >>>>>> 'MTP Templates' moved to Xcode templates"
#rm -R MTP\ Templates
rm -R screenshots
rm -R README.md


echo "   >>>>>> extra files removed"
rm -- "$0"

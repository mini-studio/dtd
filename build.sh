#!/bin/sh
#export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.7.0_51.jdk/Contents/Home
#export PATH=$JAVA_HOME/bin:$PATH
DATE=`date +%Y%m%d%H%M`

function build()
{
    build_gradle=$1;
    vcode=$2;
    version=$3;
    manifest=$4;
    mode=$5;
    target=$6;
    name=$7;
    oname=$8;
    channel = $9;
    appId = $10;

    echo "$build_gradle $vcode $version $manifest $mode $target"
    mv Library/build.gradle Library/build.gradle.bck
    mv Library/$1 Library/build.gradle
    sed -i '' "s/versionCode [^\s]*/versionCode $vcode/g" Library/build.gradle
    sed -i '' "s/versionName \"[^\"]*\"/versionName \"$version\"/g" Library/build.gradle

    sed -i '' "s/app_build:[0-9]*/app_build:$DATE/g" Library/src/main/$manifest
    sed -i '' "s/android:versionCode=\"[^\"]*\"/android:versionCode=\"$vcode\"/g" Library/src/main/$manifest
    sed -i '' "s/android:versionName=\"[^\"]*\"/android:versionName=\"$version\"/g" Library/src/main/$manifest
    sed -i '' "s/app_mode:[^\"]*/app_mode:$mode/g" Library/src/main/$manifest
    sed -i '' "s/app_channel:[^\"]*/app_channel:$channel/g" Library/src/main/$manifest
    sed -i '' "s/appId:[^\"]*/appId:$appId/g" Library/src/main/$manifest

    if [ "" != "$name" ]; then
        sed -i '' "s/@string\/$oname/$name/g" Library/src/main/$manifest
    fi

    echo '========building parent package=========='
    gradle Library:assembleRelease
    mv Library/build.gradle Library/$build_gradle
    mv Library/build.gradle.bck Library/build.gradle
    if [ "" == "$name" ]; then
        mv Library/build/outputs/apk/Library-release.apk target/$target-V$version-$mode-"(Build."$DATE").apk"
    else
        mv Library/build/outputs/apk/Library-release.apk target/$target-V$version-$mode-$name-"(Build."$DATE").apk"
    fi

    sed -i '' "s/app_mode:[^\"]*/app_mode:Sandbox/g" Library/src/main/$manifest

    sed -i '' "s/app_channel:[^\"]*/app_channel:/g" Library/src/main/$manifest

    sed -i '' "s/appId:[^\"]*/appId:/g" Library/src/main/$manifest

    if [ "" != "$name" ]; then
    sed -i '' "s/$name/@string\/$oname/g" Library/src/main/$manifest
    fi
}
rm -fr Library/build/outputs/*
rm -fr target
mkdir target
for line in $(cat ./build_configs.txt)
 do
 echo $line
    build_gradle=`echo $line|cut -f1 -d','`
    if [ "${build_gradle:0:1}" = "#" ]; then
		continue
	fi
 	manifest=`echo $line|cut -f2 -d','`
 	mode=`echo $line|cut -f3 -d','`
 	target=`echo $line|cut -f4 -d','`
 	vcode=`echo $line|cut -f5 -d','`
 	version=`echo $line|cut -f6 -d','`
 	name=`echo $line|cut -f7 -d','`
 	oname=`echo $line|cut -f8 -d','`
 	channel=`echo $line|cut -f9 -d','`
 	appId=`echo $line|cut -f10 -d','`
 	if ( [ $mode != 'Debug' ] && [ $mode != 'Release' ] && [ $mode != 'Sandbox' ] ); then
 	   echo "mode [$mode] is incorrect"
 	   exit
 	fi
	build $build_gradle $vcode $version $manifest $mode $target $name $oname $channel $appId
 done


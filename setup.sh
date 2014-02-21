#!/bin/sh

echo "Running setup [ ${ANDROID_HOME} ]"

echo "Installing Google Play Services..."
cp script/gms-mvn-install.sh $ANDROID_HOME/extras/google/google_play_services/libproject/google-play-services_lib
pushd $ANDROID_HOME/extras/google/google_play_services/libproject/google-play-services_lib
sh gms-mvn-install.sh 11
popd


echo "Installing android support library..."
mvn -q install:install-file -DgroupId=com.android.support -DartifactId=support-v4 \
	-Dversion=19.0.0 -Dpackaging=jar -Dfile="$ANDROID_HOME/extras/android/support/v4/android-support-v4.jar"


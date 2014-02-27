

all:
	mvn clean package android:deploy android:run && cp target/generated-sources/r/com/tddrampup/R.java gen/com/tddrampup/

setup:
	./setup.sh

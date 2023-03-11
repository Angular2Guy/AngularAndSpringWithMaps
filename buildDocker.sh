#!/bin/sh
#./gradlew clean build -PwithAngular=true -PuseChromium=true
./gradlew clean build -PwithAngular=true
docker build -t angular2guy/angularandspringwithmaps:latest --build-arg JAR_FILE=angularAndSpringWithMaps.jar --no-cache .
docker run -p 8080:8080 --memory="384m" --network="host" angular2guy/angularandspringwithmaps:latest
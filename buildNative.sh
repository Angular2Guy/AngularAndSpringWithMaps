#!/bin/bash
# add Spring Boot Aot/Hibernate Enhancement plugin
# install graalvm
# fix native build environment(missing libs)
# set prod profile in application.properties
# normal build
./gradlew clean build  -PwithAngular=true -PuseChromium=true
# run with native-image-agent to generate the 'native-image' config files
java -Dspring.aot.enabled=true -agentlib:native-image-agent=config-merge-dir=backend/src/main/resources/META-INF/native-image -jar ./backend/build/libs/angularAndSpringWithMaps.jar --spring.profiles.active=prod
# liquibase data/*.csv files have to be included in META-INF/native-image/resource-config.json
#    {
#      "pattern":"\\Qdbchangelog/data/company_site.csv\\E"
#    },  
#    {
#      "pattern":"\\Qdbchangelog/data/location.csv\\E"
#    },
#    {
#      "pattern":"\\Qdbchangelog/data/polygon.csv\\E"
#    },
#    {
#      "pattern":"\\Qdbchangelog/data/ring.csv\\E"
#    },
# native build
./gradlew clean nativeCompile -PwithAngular=true -PuseChromium=true
# run native binary(more than 240MB)
./backend/build/native/nativeCompile/backend
# build Docker image
docker build -t angular2guy/angularandspringwithmaps-native:latest --build-arg APP_FILE=build/native/nativeCompile/backend --no-cache .
# run Docker image
docker run -p 8080:8080 --memory="300m" --network="host" angular2guy/angularandspringwithmaps-native:latest
#!/bin/sh
docker build -t angular2guy/angularandspringwithmaps:latest --build-arg JAR_FILE=build/libs/angularAndSpringWithMaps.jar --no-cache .
docker run -p 8080:8080 --memory="1g" --network="host" angular2guy/angularandspringwithmaps:latest
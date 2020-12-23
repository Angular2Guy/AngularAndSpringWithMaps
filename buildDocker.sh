#!/bin/sh
docker build -t angular2guy/angularandspringwithmaps:latest --build-arg JAR_FILE=build/libs/angularAndSpringWithMaps.jar --no-cache .
docker run -p 8080:8080 --network="host" angular2guy/angularandspringwithmaps:latest
docker run --name maps-postgres -e POSTGRES_PASSWORD=sven1 -e POSTGRES_USER=sven1 -e POSTGRES_DB=maps -p 5432:5432 -d postgres
# docker start maps-postgres
# docker stop maps-postgres
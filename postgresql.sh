#!/bin/sh
docker pull postgres
docker run --name maps-postgres -e POSTGRES_PASSWORD=sven1 -e POSTGRES_USER=sven1 -e POSTGRES_DB=maps -p 5432:5432 -d postgres
# docker start maps-postgres
# docker stop maps-postgres
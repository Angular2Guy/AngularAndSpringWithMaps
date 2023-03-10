docker pull postgres:14
docker run --name local-postgres-maps -e POSTGRES_PASSWORD=sven1 -e POSTGRES_USER=sven1 -e POSTGRES_DB=maps -p 5432:5432 -d postgres:14

# docker start local-postgres-maps
# docker stop local-postgres-maps
# docker exec -it local-postgres-maps bash
# pg_dump -h localhost -U sven1 -d maps -c > portfolioMgr.sql
# psql -h localhost -U sven1 -d maps < portfolioMgr.sql
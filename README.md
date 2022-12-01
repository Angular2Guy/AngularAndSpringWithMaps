# AngularAndSpringWithMaps

<!-- ![Build Status](https://travis-ci.org/Angular2Guy/AngularAndSpringWithMaps.svg?branch=master)-->

Author: Sven Loesekann

Technologies: Angular, Spring Boot, Java, Gradle, GraphQl, GraphiQl, Typescript, Angular Cli, Angular Material, H2/Postgresql Databases, Jpa, Bing Maps

## Articles
* [Spring Boot 3 update experience](https://angular2guy.wordpress.com/2022/11/15/spring-boot-3-update-experience/)
* [Graphql in Mircoservices](https://angular2guy.wordpress.com/2022/10/24/graphql-in-microservices/)
* [Bing Maps With Angular in a Spring Boot Application](https://angular2guy.wordpress.com/2021/07/31/bing-maps-with-angular-in-a-spring-boot-application/)
* [Using Bing Maps to add Shapes With Angular in a Spring Boot Application](https://angular2guy.wordpress.com/2021/07/31/using-bing-maps-to-add-shapes-with-angular-in-a-spring-boot-application/)
* [Using Bing Maps to remove Shapes With Angular in a Spring Boot Application](https://angular2guy.wordpress.com/2021/07/31/using-bing-maps-to-remove-shapes-with-angular-in-a-spring-boot-application/)

## Features
- Integration of Angular and Bing Maps
- Load/Save property borders from GraphQl interface
- Show different border at different points in time
- Select different sites
- read/edit/delete site data with GraphQl
- Load/Save the site data in the H2/Postgresql Databases

## Mission Statement
The project serves as an example howto integrate Angular and Bing Maps with Spring Boot and relational databases. The frontend shows different property borders at different points in time for different company sites. The backend stores multiple company sites at different points in time with multiple property borders in clean architecture. Graphql is used to read and write the data in this project. The backend manages/initialzies the H2/Postgresql databases with Liquibase. The data access is done with Jpa and Spring Repositories. The architecture is checked with ArchUnit in a test.

## Rest Branch
The previous version of the project has used Rest endpoints. That version is available in the [Rest Branch](https://github.com/Angular2Guy/AngularAndSpringWithMaps/tree/rest).

## GraphQl
The data is read and stored with GraphQl. The frontend uses the Angular http client to access the GraphQl endpoint. The backend uses Spring GraphQl to provide the endpoint with the schema. GraphiQl provides a Ui to test the endpoint.

## Postgresql
In the postgresql.sh file are the commands to pull and run Postgresql in a Docker image locally. To build a Jar with Postgresql setup build it with 'gradlew build -PwithAngular=true'. The Spring Boot jar can then be started with the VM parameter '-Dspring.profiles.active=prod' and the 'BINGMAPKEY' has to availiable in an environment variable. The database will be initialized by Liquibase.

## Bing Maps Key
A Bing Maps Key is availiable for development use here: https://www.bingmapsportal.com

## Testdata
The testdata for the company site is Airbus Finkenwerder/Toulouse for the year 2020. The testdata for the year 2010 is dummy data. 

## Kubernetes setup
In the helm directory is a kubernetes setup to run the angularandspringwithmaps project with minikube. The Helm chart deployes the postgres database and the angularandspringwithmaps with the needed parameters to run. It uses the resource limit support of Jdk 16 to limit memory. Kubernetes limits the cpu use and uses the startupprobes and livenessprobes that Spring Actuator provides.

## Monitoring
The Spring Actuator interface with Prometheus interface can be used as it is described in this article: 

[Monitoring Spring Boot with Prometheus and Grafana](https://ordina-jworks.github.io/monitoring/2020/11/16/monitoring-spring-prometheus-grafana.html)

To test the setup the application has to be started and the Docker Images for Prometheus and Grafana have to be started and configured. The scripts 'runGraphana.sh' and 'runPrometheus.sh' can be used as a starting point.

## Example
An example Graphql query/params with all fields:
```
{query getCompanySiteByTitle($title: String!, $year: Long!) { 
   getCompanySiteByTitle(title: $title, year: $year) { id, title, atDate, polygons 
      { id, fillColor, borderColor, title, longitude, latitude, rings 
         { id, primaryRing, locations 
            { id, longitude, latitude }
         }
       }
     }
   }
}
```

```
{ 'title': 'Finkenwerder', 'year': 2020 }
```

Spring Graphql executes it with 4 sql queries. One query for the CompanySites, Polygons, Rings and Locations.

## Setup
Java 17 or newer.

Postgresql 12.x or newer.

Eclipse IDE for Enterprise Java and Web Developers newest version.

Install Eclipse Plugin 'Eclipse Wild Web Developer' of the Eclipse Marketplace.

Gradle 7.5 or newer.

Nodejs 14.20.x or newer

Npm 6.13.x or newer

Angular Cli 15 or newer.

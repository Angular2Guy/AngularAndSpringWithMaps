# AngularAndSpringWithMaps

<!-- ![Build Status](https://travis-ci.org/Angular2Guy/AngularAndSpringWithMaps.svg?branch=master)-->

Author: Sven Loesekann

Technologies: Angular, Spring Boot, Java, Gradle, Typescript, Angular Cli, Angular Material, H2/Postgresql Databases, Jpa, Bing Maps

## Articles
* [Bing Maps With Angular in a Spring Boot Application](https://angular2guy.wordpress.com/2021/07/31/bing-maps-with-angular-in-a-spring-boot-application/)
* [Using Bing Maps to add Shapes With Angular in a Spring Boot Application](https://dzone.com/articles/Using-Bing-Maps-to-add-Shapes-With-Angular-in-a-Spring-Boot-Application)
* [Using Bing Maps to remove Shapes With Angular in a Spring Boot Application](https://angular2guy.wordpress.com/2021/07/31/using-bing-maps-to-remove-shapes-with-angular-in-a-spring-boot-application/)

## Features
- Integration of Angular and Bing Maps
- Load/Save property borders from rest endpoint
- Show different border at different points in time
- Select different sites
- get/post site data to rest endpoint
- Load/Save the site data in the H2/Postgresql Databases

## Mission Statement
The project serves as an example howto integrate Angular and Bing Maps with Spring Boot and relational databases. The frontend shows different property borders at different points in time for different company sites. The backend stores multiple company sites at different points in time with multiple property borders in clean architecture. The backend manages/initialzies the H2/Postgresql databases with Liquibase. The data access is done with Jpa and Spring Repositories. The architecture is checked with ArchUnit in a test.

## Postgresql
In the postgresql.sh file are the commands to pull and run Postgresql in a Docker image locally. To build a Jar with Postgresql setup build it with 'gradlew build -PwithAngular=true'. The Spring Boot jar can then be started with the VM parameter '-Dspring.profiles.active=prod' and the 'BINGMAPKEY' has to availiable in an environment variable. The database will be initialized by Liquibase.

## Bing Maps Key
A Bing Maps Key is availiable for development use here: https://www.bingmapsportal.com

## Testdata
The testdata for the company site is Airbus Finkenwerder for the year 2020. The testdata for the year 2010 is dummy data. 

## Kubernetes setup
In the helm directory is a kubernetes setup to run the angularandspringwithmaps project with minikube. The Helm chart deployes the postgres database and the angularandspringwithmaps with the needed parameters to run.

## Setup
Java 11 or newer.

Postgresql 9.x or newer.

Eclipse Oxygen JEE or newer.

Install Eclipse Plugin 'Eclipse Wild Web Developer' of the Eclipse Marketplace.

Gradle 6.6 or newer.

Nodejs 12.16.x or newer

Npm 6.13.x or newer

Angular Cli 12 or newer.

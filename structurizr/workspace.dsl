workspace "AngularAndSpringWithMaps" "The project shows howto integrate Bing maps with an Angular Frontend and a Spring Boot Backend." {

    model {
        user = person "User"
        angularAndSpringWithMapsSystem = softwareSystem "AngularAndSpring WithMaps System" "AngularAndSpringWithMaps System to manage locations on a map." {
        	angularAndSpringWithMaps = container "AngularAndSpring WithMaps" "SPA with Angular and Bing Maps and a Spring Boot Backend." {
        		companySiteController = component "CompanySiteController" "Graphql controller for the companysites with its child elements."
        		companySiteService = component "CompanySiteService" "Implements the companysite logic for different graphs."
        		companySiteRepositories = component "CompanySite Repositories" "CompanySite repository with its child repositories."
        	}
        	database = container "Postgresql Db" "Postgresql stores all the data of the system." tag "Database"
		}
		# relationships people / software systems
        user -> angularAndSpringWithMapsSystem "manages locations"
        
        # relationships containers
        user -> angularAndSpringWithMaps
        angularAndSpringWithMaps -> database
        
        # relationships components
        companySiteController -> companySiteService
        companySiteService -> companySiteRepositories
    }

    views {
        systemContext angularAndSpringWithMapsSystem "SystemContext" {
            include *
            autoLayout
        }
        
        container angularAndSpringWithMapsSystem "Containers" {
        	include *
            autoLayout lr
        }
        
        component angularAndSpringWithMaps "Components" {
        	include *
            autoLayout
        } 
        
        styles {
        	element "Person" {            
            	shape Person
        	}
        	element "Database" {
                shape Cylinder                
            }
            element "Browser" {
                shape WebBrowser
            }
        }
    }

}
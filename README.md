CodePump #2
=============

[ ![Codeship Status for TKasekamp/Veebirakendus](https://codeship.io/projects/0611c2e0-444e-0132-c733-5ea9e4cf64c6/status)](https://codeship.io/projects/44818)

##English
This webapp was created in University of Tartu during spring 2014 as part of [MTAT.03.230 Web Application Development] (https://courses.cs.ut.ee/2014/vl/spring).

####Project
We wanted to create something like pastebin where you put code and share it with everyone. Link to app - [codepump2.herokuapp.com] (codepump2.herokuapp.com).

####Team
######Team lead, backend, frontend and database developer: 
Tõnis Kasekamp
######Front and backend developer:
Juhan-Rasmus Risti
######Frontend developer:
Sander Tiganik

####Technologies used
* Maven as build tool
* Jetty 9.1.2 webserver
* PostgreSQL 9.3 database from Heroku
* Hibernate 4.3.4 to handle database connection
* Hibernate Search 4.5.1 and Lucane for searching
* c3p0 4.3.5.Final for connection pooling
* Velocity 1.7 as the templating engine
* Google Gson 2.2.4 for JSON parsing
* Google Guice 3.0 for dependency injection
* Mockito 1.9.5 for testing
* JUnit 4.11 for testing
* Selenium for testing

Check pom.xml for more.
####Running the app
The app is by default going to connect to a local database (or Heroku database if the app is deployed in Heroku) using hibernate.cfg.xml properties. If you have no local database, set USE_DATABASE to false in com.codepump.controller.ServerController. This will activate server memory mode. Basic functions will work, but not everything.
 
Navigate to the Veebirakendus folder in command line. This will work if you have correctly installed Maven.
```
mvn package
mvn jetty:run
```
The webapp will be accessible from localhost:8080. 


##Eesti keeles
Veebirakenduste loomine (MTAT.03.230)

Projekti teema: Koodikorv

Rühmaliikmed: Juhan-Rasmus Risti, Sander Tiganik, Tõnis Kasekamp.

Planeeritud funktsioonid:
* Kasutajasüsteem: registreerimine, sisselogimine. TEHTUD
* Teksti kirjutamine/kleepimine tekstikasti. TEHTUD
* Koodi süntaksi highlight'imise võimalus. TEHTUD
* Teksti eluaja määramine: "__ hours", "__ days", "Forever".
* Teksti ligipääsetavuse määramine: "Public", "Unlisted" ehk "Private". TEHTUD
* Teksti salvestamine ja sidumine oma kasutajaga. TEHTUD
* Isiklike tekstide kategoriseerimine ja kuvamine. TEHTUD
* Isiklike tekstide ligipääsetavuse muutmine ja kustutamine. TEHTUD
* Avalike tekstide otsimine sõna järgi. TEHTUD
* Avalike tekstide kuvamine ja sorteerimine kategooria või kuupäeva järgi.

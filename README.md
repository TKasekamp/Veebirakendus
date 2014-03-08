CodePump #2
=============

Veebirakenduste loomine (MTAT.03.230)

Projekti teema: Koodikorv

Rühmaliikmed: Juhan-Rasmus Risti, Sander Tiganik, Tõnis Kasekamp.

Planeeritud funktsioonid:
* Kasutajasüsteem: registreerimine, sisselogimine.
* Teksti kirjutamine/kleepimine tekstikasti.
* Koodi süntaksi highlight'imise võimalus.
* Teksti eluaja määramine: "__ hours", "__ days", "Forever".
* Teksti ligipääsetavuse määramine: "Public", "Unlisted", "Password Protected".
* Teksti salvestamine ja sidumine oma kasutajaga.
* Isiklike tekstide kategoriseerimine ja kuvamine.
* Isiklike tekstide ligipääsetavuse muutmine ja kustutamine.
* Avalike tekstide otsimine sõna järgi.
* Avalike tekstide kuvamine ja sorteerimine kategooria või kuupäeva järgi.

Käivitamine:
Mine käsurealt kausta Veebirakendus. 
mvn package && java -jar target/dependency/jetty-runner.jar --port 8080 target/*.war
mvn jetty:run
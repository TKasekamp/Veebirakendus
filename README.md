CodePump #2
=============

Veebirakenduste loomine (MTAT.03.230)

Projekti teema: Koodikorv

Rühmaliikmed: Juhan-Rasmus Risti, Sander Tiganik, Tõnis Kasekamp.

Planeeritud funktsioonid:
* Kasutajasüsteem: registreerimine, sisselogimine. TEHTUD
* Teksti kirjutamine/kleepimine tekstikasti. TEHTUD
* Koodi süntaksi highlight'imise võimalus. TEHTUD
* Teksti eluaja määramine: "__ hours", "__ days", "Forever".
* Teksti ligipääsetavuse määramine: "Public", "Unlisted", "Password Protected". 
* Teksti salvestamine ja sidumine oma kasutajaga. TEHTUD
* Isiklike tekstide kategoriseerimine ja kuvamine. 
* Isiklike tekstide ligipääsetavuse muutmine ja kustutamine.
* Avalike tekstide otsimine sõna järgi.
* Avalike tekstide kuvamine ja sorteerimine kategooria või kuupäeva järgi.

##Käivitamine:
Mine käsurealt kausta Veebirakendus. See eeldab, et Maven on paigaldatud.
```
mvn package
mvn jetty:run
```
Lehekülg asub nüüd aadressil localhost:8080.

##Andmebaas
Hetkel pakkub codepump kolme kohta andmete hoidmiseks:
* Serveri mälu
* Localhost andmebaas
* Heroku andmebaas

Alla tõmmates on codepump seadistatud kasutama Heroku andmebaasi. Heroku andmebaasiga ühendamine võtab aega umbes 2 minutit ja 40 sekundit. Kui keegi oskab seda aega vähendada, siis palun võtke ühendust. Localhost seadistus asub hibernate.hbm.xml failis, kuid on välja kommenteeritud.
Serveri mälu saab kasutada, kui klassis com.codepump.controller.Servercontroller seada USE_DATABASE = false. Põhifunktsioonid nagu koodi tekitamine, kasutaja loomine, sisse- ja väljalogimine töötavad serveri mälus, kuid nende arendamine pole prioriteet.

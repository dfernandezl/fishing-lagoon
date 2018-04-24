ENTREGA DE LABORATORI DE SOFTWARE I
-----------------------------------

Repositori origen:
- https://github.com/TC-LS1/fishin-lagoon-...

Nota: 
Abans d'entregar modifica també el pom.xml i a 
<name> poseu el nom del repositori.

## INTEGRANTS

- Nom i Cognoms:
- User Github:
 
 
- Nom i Cognoms:
- User Github:


Participacio:

- Nom i %:
- Nom i %:


## Estrategies

### com.drpicox.strategy.drpicox.OneStrategy

En cada setmana pesca un peix.


### com.drpicox.strategy.drpicox.PercentStrategy

Donat un tant percent inicial, 
cada setmana pesca el percentatge
de peixos restant.
Te en compte els peixos que ell
mateix ha pescat.


### com.drpicox.strategy.drpicox.PowerStrategy

En cada setmana pesca el cuadrat de 
peixos que la setmana que és.


### com.drpicox.strategy.drpicox.RestStrategy

Només descansa.


### com.drpicox.strategy.drpicox.TitForTatStrategy

"Donde las dan las toman" strategy.

En aquesta estrategia s'analitza el que ha pasat
la ronda anterior i es marca com a traidor aquell
en que en la primera setmana ha pescat més de la
part de peixos que li corresponien. 

Per sentarse, intenta trobar un llac
on no hi hagi cap traidor.

Per donar les ordres, mira si li ha tocat cap 
traidor al llac on esta sentat.  
Si hi ha cap traidor usa una estratègia de traidor.
Si no hi ha cap traidor usa una estratègia col·laborativa.

En acabar la ronda, analitza per cada bot si ha
pescat en la primera setmana més del que li 
correspondria, i si es així el marca com a traidor
per a les properes rondes.


## Tournaments

### tournament1

Prova a la lalala de varies estrategies.

### tournament2

Prova a la lalala de poques estrategies.

### tournament3 i 4

Proven la estrategia tit for tat de la següent manera:

- Tournament 3 s'inclou una estrategia 
  col·laboracionista en primera ronda
  
- Tournament 4 inclou una estrategia
  competitiva
  
A mes, s'afegeixen dues instancies de la estrategia 
tit for tat per poder comprovar si és capaç de 
col·laborar amb si mateixa.

Al tournament 3 es veu que aconsegueixen ratis molt
elevats de puntuació al col·laborar.

Al tournament 4 es veu que traicionen a la altre
estrategia apartir de la segona ronda ja traicionen
a la estrategia traicionera, evitant que faci masses 
punts, i en la tercera ronda si poden juguen soles
sense els altres i intenten fer la maxima puntuació.



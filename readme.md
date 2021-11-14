# Tetris

## I. Introduction

Nous avons implémenté une version à deux joueurs du jeu tetris. Pour se faire nous nous somme basé
sur un model simplifié du jeu de base, dans lequel nous avons ajouté un système de client/server. Le
résultat est jeu ou 2 joueurs peuvent s'affronter dans une partie de Tetris.

## II. Règles et choix d'implementations.

l'explication du jeu se trouve ici : https://fr.wikipedia.org/wiki/Tetris

Nous nous sommes écarter des règles de bases sur plusieurs point pour limité l'étendue du
dévelloppement.

1. Pas de règle de malus à envoyer à l'adversaire.

## II. Contrainte

### 1ère itération.

Nous devions implémenter dans notre code, l'utilisation des threads ainsi qu'un système de
client/server.

#### Client/Server

Pour le client/server, nous avons dévellopé une première application server qui centralise les
échanges entre les 2 clients. Un client se connecte au server, préalablement démarrer et lorsque les
2ème client se connecte, la partie peut démarrer

#### Thread

Nous avons implémenté les threads à 2 endroits dans notre code.

1. Les tétrominos chutes automatiquement toute les x secondes ce mécanisme est géré dans un thread a
   part entière basé sur un timer.
2. Les communications client/server sont géré dans un autre thread.

## Bugs connus
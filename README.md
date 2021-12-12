# Tetris

## Auteurs

- Andrew SASSOYE
- Constantin GUNDUZ
- Gregory VAN DER PLUIJM
- Thomas LEUTSCHER

## Maven

### Compiler

```
$ mvn clean package
```

### Tests unitaires

```
$ mvn verify
```

### Executer le serveur

```
$ java -jar src/server/target/tetris.server-2.0.jar
```

### Executer le client

```
$ java -jar src/client/target/tetris.client-2.0.jar
```

Avec des parametres

```
$ java -jar src/client/target/tetris.client-2.0.jar --host=127.0.0.1 --port=6969 --username=Pacliclown
```

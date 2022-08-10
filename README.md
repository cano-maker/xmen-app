# xmen-app Challenge Mercado Libre

Este proyecto usa Quarkus como framework de desarrollo para Java

This project uses Quarkus, the Supersonic Subatomic Java Framework( https://quarkus.io/)
esta contruido utilizando diferentes paradigmas de programacion como Programacion funcional,
reactiva y programacion orientada a objetos.

Se utiliza Google App Engine para alojar el API desarrollada y una base de datos PostgreSQL.

> **_NOTE:_**  actualmente la base de datos funciona unicamente de forma local, en un futuro estara en CloudSQL

## Requisitos

Se necesita instalar los siguientes programas:
- Docker : https://docs.docker.com/engine/install/
- Cualquier SQL Client, se recomienda DBeaver(https://dbeaver.io/)

## Configuracion PostgreSQL en Docker

- Abrir una consola y ubicarse en la ruta `../xmen-app/devops/local/pgsql` ejecutar el comando siguente para instalar la imagen de PostgreSQL
```shell script
docker-compose up -d
```
- Conectarse a PostgreSQL con el SQL Client e ingresar el usuario y la contraseña `postgres`.
- Crear la base de datos `dnarecord`

## Correr la applicacion 

Correr la aplicación utilizando el comando:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Se puede accerder a la interfaz de usuariode Quarkus en la ruta: http://localhost:8080/q/dev/.


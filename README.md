# eng-zap-challenge-scala

Código Desafio Grupo ZAP

## Requisitos

- java 1.8.0+
- sbt 1.2.8+
- docker-compose 1.25.0+

## Instalação

Após clonar o projeto.
```
$ cd eng-zap-challenge-scala/
```
```
$ sbt clean assembly
```

Executar os comandos para levantar os conteiners docker do kafka e zookeeper:
```
$ cd docker/
```
```
$  docker-compose up -d
```
```
$  docker-compose ps
```
```
       Name                   Command            State                        Ports                      
---------------------------------------------------------------------------------------------------------
docker_kafka_1       /etc/confluent/docker/run   Up      0.0.0.0:29092->29092/tcp, 0.0.0.0:9092->9092/tcp
docker_zookeeper_1   /etc/confluent/docker/run   Up      0.0.0.0:2181->2181/tcp, 2888/tcp, 3888/tcp
```

Executar os comandos para criar os tópicos necessários:

```
$ docker-compose exec kafka kafka-topics --create --topic output-zap --partitions 1 --replication-factor 1 --if-not-exists --zookeeper zookeeper:2181
```
```
$ docker-compose exec kafka kafka-topics --create --topic output-viva --partitions 1 --replication-factor 1 --if-not-exists --zookeeper zookeeper:2181
```

## Testes

```
$ sbt test
```

## Scripts para Manipular a Aplicação

### Deploy :

```
$ ./bin/app.sh deploy
```

### Zap : 

Producer
```
$ ./bin/app.sh zap-producer
```

Consumer
```
$ ./bin/app.sh zap-consumer
```
### Viva :

Producer :
```
$ ./bin/app.sh viva-producer
```

Consumer
```
$ ./bin/app.sh viva-consumer
```

### Stop

```
$ ./bin/app.sh stop
```


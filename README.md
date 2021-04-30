# eng-zap-challenge-java

Código Desafio Grupo ZAP

## Requisitos

 Ter o docker-compose version 1.25.0+ instalado.

## Instalação

Após clonar o projeto.
```
$ cd eng-zap-challenge-scala/
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

Executar os comandos para criar os tópicos necessários.

```
$ docker-compose exec kafka kafka-topics --create --topic output-zap --partitions 1 --replication-factor 1 --if-not-exists --zookeeper zookeeper:2181
```
```
$ docker-compose exec kafka kafka-topics --create --topic output-viva --partitions 1 --replication-factor 1 --if-not-exists --zookeeper zookeeper:2181
```

## Compilação

Para efeito de compilação é necessário ter o "sbt 1.2.8+" instalado. Porém já possui uma compilação pronta para ser executada: bin/Eng-Zap-Challenge-Scala-assembly-0.1.jar

```
$ sbt clean assembly
```

## Scripts para manipular a aplicação

### Deploy :

Precisa se atentar ao item da compilação.

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



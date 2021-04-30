#!/bin/bash

PROCESS=`ps -ef | grep -i 'Eng-Zap-Challenge' | grep -v grep | awk '{print $2}'`

case $1 in
	stop)
      echo '(0^0) PROCESS STOPPED ....'
    	kill -9 $PROCESS
    ;;
	zap-producer)
      echo '(0^0) ZAP PRODUCER STARTED ....'
    	java -cp bin/Eng-Zap-Challenge-Scala-assembly-0.1.jar br.com.zap.controller.ZapProducerController
    ;;
  zap-consumer)
      echo '(0^0) ZAP CONSUMER STARTED ....'
    	java -cp bin/Eng-Zap-Challenge-Scala-assembly-0.1.jar br.com.zap.controller.ZapConsumerController
    ;;
  viva-producer)
      echo '(0^0) VIVA PRODUCER STARTED ....'
    	java -cp bin/Eng-Zap-Challenge-Scala-assembly-0.1.jar br.com.zap.controller.VivaProducerController
    ;;
  viva-consumer)
      echo '(0^0) VIVA CONSUMER STARTED ....'
    	java -cp bin/Eng-Zap-Challenge-Scala-assembly-0.1.jar br.com.zap.controller.VivaConsumerController
    ;;
    deploy)
        echo '(0^0) APPLICATION DEPLOY ....'
        git pull origin master
        sbt clean assembly
        cp target/scala-2.12/Eng-Zap-Challenge-Scala-assembly-0.1.jar bin/
        cd docker/
        docker-compose up -d
        sleep 60
        docker-compose exec kafka kafka-topics --create --topic output-zap --partitions 1 --replication-factor 1 --if-not-exists --zookeeper zookeeper:2181
        docker-compose exec kafka kafka-topics --create --topic output-viva --partitions 1 --replication-factor 1 --if-not-exists --zookeeper zookeeper:2181
    ;;
esac


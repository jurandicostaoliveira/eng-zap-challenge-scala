#!/bin/bash

PROCESS=`ps -ef | grep -i btg360 | grep -v grep | awk '{print $2}'`
MYSQLHOST='177.153.231.93'
MYSQLUSER='root'
MYSQLPASSWORD='loca1020'

export HADOOP_CONF_DIR=/home/spark/hadoop/etc/hadoop
export SPARK_HOME=/home/spark/spark
export LD_LIBRARY_PATH=/home/spark/hadoop/lib/native:$LD_LIBRARY_PATH
export HADOOP_HDFS_HOME=/home/spark/hadoop
export HADOOP_YARN_HOME=/home/spark/hadoop
export JAVA_HOME=/usr/lib/jvm/java-1.8.0


################# INFRA ####################
##### ROTACIONAMENTO DO LOG NOHUP.OUT #####
INFRA_HORAS=$(date +%H)


if [ $INFRA_HORAS == "23" ]; then
  INFRA_DATA=$(date -d "+1 day" +%d-%m-%y)
 else
  INFRA_DATA=$(date +%d-%m-%y)
fi

############################################

sparkSubmitRun() {
        nohup \
        spark-submit \
        --class br.com.btg360.scheduling.Kernel \
        --master yarn \
        --deploy-mode client \
        --driver-memory 20g \
        --executor-memory 10g \
        --num-executors 12 \
        --executor-cores 5 \
        --conf spark.local.dir=/storage/tmp \
        --conf spark.yarn.am.waitTime=1d \
        --conf spark.yarn.historyServer.allowTracking=true \
        --conf spark.shuffle.service.enabled=true \
        --conf spark.dynamicAllocation.enabled=true \
        --conf spark.dynamicAllocation.initialExecutors=4 \
        --conf spark.dynamicAllocation.maxExecutors=12 \
        --conf spark.dynamicAllocation.minExecutors=2 \
        --conf spark.core.connection.ack.wait.timeout=6000 \
        --conf spark.storage.memoryFraction=1 \
        --conf spark.rdd.compress=true \
        --conf spark.shuffle.compress=true \
        --conf spark.shuffle.spill.compress=true \
	    --conf spark.sql.shuffle.partitions=60 \
        --conf spark.default.parallelism=60 \
        --conf spark.network.timeout=12000s \
        --conf spark.executor.heartbeatInterval=1000s \
        --conf spark.cassandra.connection.timeout_ms=60000 \
        --conf spark.cassandra.connection.keep_alive_ms=900000 \
        --conf spark.driver.extraJavaOptions='-XX:ReservedCodeCacheSize=100M -XX:MaxMetaspaceSize=256m -XX:CompressedClassSpaceSize=256m' \
        --conf spark.executor.extraJavaOptions='-XX:ReservedCodeCacheSize=100M -XX:MaxMetaspaceSize=256m -XX:CompressedClassSpaceSize=256m' \
	    --conf spark.scheduler.mode='FAIR' \
        --conf spark.scheduler.allocation.file=/home/Btg-Scala-Sending-Generator/src/main/resources/fairscheduler.xml \
        /home/Btg-Scala-Sending-Generator/target/scala-2.11/Btg-Scala-Sending-Generator-assembly-0.1.jar \
        >> /storage/logs/nohup-$INFRA_DATA.out &
}

case $1 in

	stop)
        echo "(0^0) PROCESS STOPPED"
        ps aux | grep btg360
    	kill -9 $PROCESS
        mysql -h$MYSQLHOST -u$MYSQLUSER -p$MYSQLPASSWORD -e 'UPDATE btg_jobs.rules_queue_multichannel SET status = 4 WHERE today = CURRENT_DATE() AND status = 5;'
        echo ">> Cluster running ..."
        jps
        /bin/bash /home/spark/hadoop/sbin/stop-all.sh
        echo ">> Cluster stopped ..."
        jps
	;;
	start)
        echo "(0_0) PROCESS STARTED"
        jps
        /bin/bash /home/spark/hadoop/sbin/start-all.sh
        echo ">> Cluster running ..."
        jps
        sleep 60
	    sparkSubmitRun
        echo "---------------"
        ps aux | grep btg360
    ;;
	restart)
        /bin/bash /home/Btg-Scala-Sending-Generator/app.sh stop
	    sleep 60
        /bin/bash /home/Btg-Scala-Sending-Generator/app.sh start
	;;
	deploy)
        /bin/bash /home/Btg-Scala-Sending-Generator/app.sh stop
        rm -f /home/Btg-Scala-Sending-Generator/target/scala-2.11/Btg-Scala-Sending-Generator-assembly-0.1.jar
        cd /home/Btg-Scala-Sending-Generator/
        git pull origin cluster
        sbt assembly
        /bin/bash /home/Btg-Scala-Sending-Generator/app.sh start
	;;

esac
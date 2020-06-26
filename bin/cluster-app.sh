#!/bin/bash

PROCESS=`ps -ef | grep -i btg360 | grep -v grep | awk '{print $2}'`
MYSQLHOST='177.153.231.93'
MYSQLUSER='root'
MYSQLPASSWORD='loca1020'
ENV_PATH=/home/Btg-Scala-Sending-Generator/src/main/resources/environment.properties

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

######## TO BTG DEDICATED ENV ##############

IS_DEDICATED_ENV=`cat $ENV_PATH | grep 'IS_DEDICATED_ENV' | cut -d'=' -f2`
DEDICATED_ENV_VALUE=0
if [ $IS_DEDICATED_ENV == true ]; then
    DEDICATED_ENV_VALUE=1
fi

############################################

sparkSubmitRun() {
        nohup \
        /home/spark/spark/bin/spark-submit \
        --class br.com.btg360.scheduling.Kernel \
        --master yarn \
        --deploy-mode client \
        --driver-memory 30g \
        --executor-memory 11g \
        --num-executors 20 \
        --executor-cores 6 \
        --conf spark.local.dir=/storage/tmp \
        --conf spark.yarn.am.waitTime=1d \
        --conf spark.yarn.historyServer.allowTracking=true \
        --conf spark.shuffle.service.enabled=true \
        --conf spark.dynamicAllocation.enabled=true \
        --conf spark.dynamicAllocation.initialExecutors=20 \
        --conf spark.dynamicAllocation.maxExecutors=20 \
        --conf spark.dynamicAllocation.minExecutors=10 \
        --conf spark.core.connection.ack.wait.timeout=6000 \
        --conf spark.storage.memoryFraction=1 \
        --conf spark.rdd.compress=true \
        --conf spark.shuffle.compress=true \
        --conf spark.shuffle.spill.compress=true \
	    --conf spark.sql.shuffle.partitions=120 \
        --conf spark.default.parallelism=120 \
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
        echo ":::: PROCESS STOPPED ::::"
        ps aux | grep btg360
        echo ">> Killing Application"
    	kill -9 $PROCESS
    	rm -rf /storage/tmp/blockmgr-*
    	rm -rf /storage/tmp/spark-*
    	echo ">> Reset Queue"
        mysql -h$MYSQLHOST -u$MYSQLUSER -p$MYSQLPASSWORD -e "UPDATE btg_jobs.rules_queue_multichannel SET status=4 WHERE today=current_date() AND status=5 AND userId IN(SELECT id FROM master.users WHERE homologation=1 AND isMultiChannel=1 AND isDedicatedEnv=$DEDICATED_ENV_VALUE);"
        echo ">> Cluster Stopped"
        /bin/bash /home/spark/hadoop/sbin/stop-all.sh
        jps
	;;
	start)
        echo ":::: PROCESS STARTED ::::"
        /bin/bash /home/spark/hadoop/sbin/start-all.sh
        echo ">> Cluster Running"
        jps
        sleep 60
	    sparkSubmitRun
        echo ">> Application Running"
        ps aux | grep btg360
    ;;
	restart)
        /bin/bash /home/Btg-Scala-Sending-Generator/bin/cluster-app.sh stop
	    sleep 60
        /bin/bash /home/Btg-Scala-Sending-Generator/bin/cluster-app.sh start
	;;
	deploy)
	    echo ":::: PROCESS DEPLOYED ::::"
	    echo ">> Delete Jar"
        rm -f /home/Btg-Scala-Sending-Generator/target/scala-2.11/Btg-Scala-Sending-Generator-assembly-0.1.jar
        echo ">> Entering the Directory"
        cd /home/Btg-Scala-Sending-Generator/
        echo ">> Running Git Pull Command"
        git pull origin master
        echo ">> Compiling Application"
        sbt clean assembly
        /bin/bash /home/Btg-Scala-Sending-Generator/bin/cluster-app.sh restart
	;;
esac
#!/bin/bash

PROCESS=`ps -ef | grep -i btg360 | grep -v grep | awk '{print $2}'`
MYSQLHOST='177.153.231.93'
MYSQLUSER='root'
MYSQLPASSWORD='loca1020'

################# INFRA ####################
##### ROTACIONAMENTO DO LOG NOHUP.OUT #####
INFRA_HORAS=$(date +%H)


if [ $INFRA_HORAS == "23" ]; then
  INFRA_DATA=$(date -d "+1 day" +%d-%m-%y)
 else
  INFRA_DATA=$(date +%d-%m-%y)
fi

############################################

case $1 in
	stop)
        echo '(0^0) PROCESS STOPPED'
    	kill -9 $PROCESS
        mysql -h$MYSQLHOST -u$MYSQLUSER -p$MYSQLPASSWORD -e 'UPDATE btg_jobs.rules_queue_multichannel SET status=4 WHERE today=current_date() AND status=5 AND userId IN(SELECT id FROM master.users WHERE homologation=1 AND isMultiChannel=1 AND isDedicatedEnv=1);'
	;;
	start)
        echo '(0_0) PROCESS STARTED'
    	nohup java -cp -Xms45000m -Xmx70000m -XX:-UseGCOverheadLimit /home/Btg-Scala-Sending-Generator/target/scala-2.11/Btg-Scala-Sending-Generator-assembly-0.1.jar br.com.btg360.scheduling.Kernel >> /storage/logs/nohup-$INFRA_DATA.out &
        echo '----------------------------------------'
        ps -ef | grep -i btg360 | grep -v grep
    ;;
	restart)
	    /bin/bash /home/Btg-Scala-Sending-Generator/bin/local-app.sh stop
        sleep 20
        /bin/bash /home/Btg-Scala-Sending-Generator/bin/local-app.sh start
    ;;
    deploy)
        rm -f /home/Btg-Scala-Sending-Generator/target/scala-2.11/Btg-Scala-Sending-Generator-assembly-0.1.jar
        cd /home/Btg-Scala-Sending-Generator/
        git pull origin cluster
        sbt assembly
        /bin/bash /home/Btg-Scala-Sending-Generator/bin/local-app.sh restart
    ;;
esac

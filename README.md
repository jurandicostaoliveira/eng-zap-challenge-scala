# Btg-Scala-Sending-Generator

Sending generator to postimatic transational

## Installation

 To inform the environment that will run the application, create a file named .development inside the src/main/resources/ directory and set the variables APP_ENV = [development*, homologation, production, cluster] and IS_DEDICATED_ENV = [true, false*]

```
$ echo -e "APP_ENV=development\nIS_DEDICATED_ENV=false" > src/main/resources/environment.properties
```

## Compilation

To compile the project, enter the project's directory and execute the command below

```
$ sbt assembly
```

#### Scripts to manipulate the application

Enter the user spark to cluster  :

```
$ su spark
```

Deploy :

```
$ /bin/bash /home/Btg-Scala-Sending-Generator/bin/[local-app or cluster-app].sh deploy
```

Stop :

```
$ /bin/bash /home/Btg-Scala-Sending-Generator/bin/[local-app or cluster-app].sh stop
```
 
Start : 

```
$ /bin/bash /home/Btg-Scala-Sending-Generator/bin/[local-app or cluster-app].sh start
```

Restart :

```
$ /bin/bash /home/Btg-Scala-Sending-Generator/bin/[local-app or cluster-app].sh restart
```



# Btg-Scala-Sending-Generator

Sending generator to postimatic transational

## Installation

 To inform the environment that will run the application, create a file named .development inside the src/main/resources/ directory and set the variable APP_ENV = [development, homologation, production]

```
$ echo -e "APP_ENV=development\nIS_DEDICATED_ENV=false" > src/main/resources/environment.properties
```

## Compilation

To compile the project, enter the project's directory and execute the command below

```
$ sbt assembly
```

#### Scripts to manipulate the application

Enter the user spark  :

```
$ su spark
```

Deploy :

```
$ /bin/bash /home/Btg-Scala-Sending-Generator/bin/app.sh deploy
```

Stop :

```
$ /bin/bash /home/Btg-Scala-Sending-Generator/bin/app.sh stop
```
 
Start : 

```
$ /bin/bash /home/Btg-Scala-Sending-Generator/bin/app.sh start
```

Restart :

```
$ /bin/bash /home/Btg-Scala-Sending-Generator/bin/app.sh restart
```



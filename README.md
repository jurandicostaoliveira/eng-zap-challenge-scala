# Btg-Scala-Sending-Generator

Sending generator to postimatic transational

## Installation

 To inform the environment that will run the application, create a file named .development inside the src/main/resources/ directory and set the variable APP_ENV = [development, homologation, production]

```
$ echo 'APP_ENV=development' > src/main/resources/.environment.properties
```

## Compilation

To compile the project, enter the project's directory and execute the command below

```
$ sbt assembly
```



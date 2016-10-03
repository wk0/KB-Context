# jena-maven-example

## requirements

- Java
- [Maven](https://maven.apache.org/)

## Setup environments

```
mvn install
```

## Run App

Edit `pom.xml` for which java file you want to exec.

For example:
 
```xml
...
<build>
    <plugins>
        <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>exec-maven-plugin</artifactId>
            <version>1.5.0</version>
            <configuration>
                <mainClass>EDIT HERE</mainClass>
            </configuration>
        </plugin>
    </plugins>
</build>
...
```

Terminal:

```
mvn exec:java
```

## dependencies

- [slf4j-api-1.7.12](http://www.slf4j.org/)
- [apache-jena-3.1.0](https://jena.apache.org/download/index.cgi)
- [exec-maven-plugin-1.5.0](http://www.mojohaus.org/exec-maven-plugin/)
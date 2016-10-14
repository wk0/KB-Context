# jena-maven-example

## Requirements

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

## Dependencies

- [slf4j-api-1.7.12](http://www.slf4j.org/)
- [apache-jena-3.1.0](https://jena.apache.org/download/index.cgi)
- [exec-maven-plugin-1.5.0](http://www.mojohaus.org/exec-maven-plugin/)

## Setup database:

First, download dataset from [http://wiki.dbpedia.org/services-resources/ontology](http://wiki.dbpedia.org/services-resources/ontology)

Quick link:
- [DBpedia Ontology T-BOX (Schema)](http://downloads.dbpedia.org/2014/dbpedia_2014.owl.bz2)
- [DBpedia Ontology RDF type statements (Instance Data)](http://downloads.dbpedia.org/2014/en/instance_types_en.nt.bz2)
- [DBpedia Ontology other A-Box properties](http://downloads.dbpedia.org/2014/en/mappingbased_properties_en.nt.bz2) (Instance Data, mapping-based properties)
- [DBpedia Ontology other A-Box specific properties](http://downloads.dbpedia.org/2014/en/specific_mappingbased_properties_en.nt.bz2) (Instance Data, mapping-based properties (specific))

Second, unzip all files and put all files to `/Store`

Third, run command below, this command will create a database which contains all data sets above. 
> This step takes around ~20 mins. Depend on your computer. 

```
tdbloader2 --loc Database/DB Store/dbpedia_2014.owl Store/instance_types_en.nt Store/mappingbased_properties_en.nt Store/specific_mappingbased_properties_en.nt
```

> Use TestDB
```
tdbloader2 --loc Database/TestDB test_data/pizza.owl.rdf test_data/cheeses-0.1.ttl
```
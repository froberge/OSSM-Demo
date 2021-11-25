# Debit Service

This Microservice was develop using [Spring Boot](https://spring.io/projects/spring-boot), [Apache Camel](https://camel.apache.org/) with [Red Hat Fuse](https://www.redhat.com/en/technologies/jboss-middleware/fuse) and [PostgreSQL](https://www.postgresql.org/)


## How to run

You need to have a PostgresSQL database. You can find the dbscripts folder.

### Run localy
```
mvn spring-boot:run -Dspring-boot.run.profiles=local
 ```

### Build on OpenShift

This will build the latest image on an ImageStream on OpenShift

```
mvn clean package fabric8:build -P openshift -Dmaven.test.skip
```

### Deploy on OpenShift

This will build, create the required resources and deploy 

```
mvn clean package fabric8:deploy -P openshift -Dmaven.test.skip
```

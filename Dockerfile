#define base docker image
FROM openjdk:11
LABEL maintainer="seerbit.com"
ADD target/transaction-statistics-0.0.1-SNAPSHOT.jar  transaction-statistics.jar
ENTRYPOINT ["java", "-jar", "transaction-statistics.jar"]
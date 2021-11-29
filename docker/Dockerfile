# https://docs.docker.com/develop/develop-images/multistage-build/
# Build a temporary image using maven
FROM maven:3.5-jdk-11 AS build
ARG RESOURCES_PATH=/src/main/resources

COPY / /

RUN mv -f $RESOURCES_PATH/application.yml.prod $RESOURCES_PATH/application.yml
WORKDIR /
RUN mvn clean package spring-boot:repackage -Dmaven.test.skip=true

FROM openjdk:11-jdk

COPY --from=build /target/steven-forest-park-springboot-0.0.1-SNAPSHOT.war /steven-forest-park-springboot.war

EXPOSE 6666
ENTRYPOINT ["java","-jar","steven-forest-park-springboot.war"]
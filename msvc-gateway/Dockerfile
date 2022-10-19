ARG MSVC_NAME=msvc-gateway

FROM openjdk:8-jdk-alpine as builder

ARG MSVC_NAME
WORKDIR /app/$MSVC_NAME

COPY ./pom.xml /app
COPY ./$MSVC_NAME/.mvn ./.mvn
COPY ./$MSVC_NAME/mvnw .
COPY ./$MSVC_NAME/pom.xml .

RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target
# RUN ./mvnw dependency:go-offline

COPY ./$MSVC_NAME/src ./src

RUN ./mvnw clean package -DskipTests


FROM openjdk:8-jdk-alpine

WORKDIR /app
RUN mkdir ./logs

ARG MSVC_NAME
ARG TARGET_FOLDER=/app/$MSVC_NAME/target
COPY --from=builder $TARGET_FOLDER/msvc-gateway-0.0.1-SNAPSHOT.jar .
ARG PORT_APP=8001
ENV PORT $PORT_APP

EXPOSE $PORT

#CMD sleep 20 $$ java -jar ./msvc-gateway-0.0.1-SNAPSHOT.jar

CMD ["java", "-jar", "./msvc-gateway-0.0.1-SNAPSHOT.jar"]
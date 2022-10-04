FROM openjdk:8

WORKDIR /app/msvc-users

COPY ./pom.xml /app
COPY ./msvc-usuarios .

RUN ./mvnw clean package -DskipTests

EXPOSE 8001

ENTRYPOINT ["java", "-jar", "./target/msvc-usuarios-0.0.1-SNAPSHOT.jar"]
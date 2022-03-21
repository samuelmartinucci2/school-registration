FROM maven:3.8.2-jdk-8 as builder

WORKDIR /app
COPY . .
RUN mvn clean install -DskipTests

FROM openjdk:8-jre-alpine

COPY --from=builder /app/target/school-registration-*.jar app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]

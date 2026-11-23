# ---- Stage 1: Build the app with Maven ----
FROM maven:3.9.6-amazoncorretto-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests -B

# ---- Stage 2: Run the app with just Java ----
FROM amazoncorretto:17
WORKDIR /app
COPY --from=build /app/target/schedule-optimizer-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
# Copy the entire project
COPY . .
# Build the server module
RUN mvn clean package -DskipTests -f server/pom.xml

# Stage 2: Run the application
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
# Copy the jar from the build stage
COPY --from=build /app/server/target/*.jar app.jar
# Expose the port (we configured 8082)
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]

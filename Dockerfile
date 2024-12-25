# Use a minimal base image with Java 17
FROM openjdk:17-jdk-slim

# Create a non-root user (optional but recommended)
RUN adduser --disabled-password --gecos '' robobobuser

USER robobobuser

# Copy the jar file from the build context
ARG JAR_FILE=target/robo-bob-1.0-SNAPSHOT.jar
COPY ${JAR_FILE} robo-bob.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/robo-bob.jar"]
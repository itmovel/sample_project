# Stage 1: Build the application
FROM maven:3.9.11-eclipse-temurin-21-noble AS build
WORKDIR /app

# Copy the custom Maven settings (access to mirror copel repo)
COPY resources/settings.xml /root/.m2/settings.xml
COPY resources/copel_artifactory.pem /tmp/corporate_cert.pem

RUN keytool -import -trustcacerts -noprompt \
    -alias copel-artifactory \
    -file /tmp/corporate_cert.pem \
    -keystore "${JAVA_HOME}/lib/security/cacerts" \
    -storepass changeit

# Copy the POM file
COPY pom.xml .

# Download all dependencies
# This is done in a separate step to leverage Docker cache
RUN mvn dependency:go-offline -B

# Copy the source code
COPY src /app/src
COPY scripts /app/scripts

# Build the application
RUN mvn package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:21-jre-noble
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar /app/app.jar

# Expose the port the app runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-Djavax.net.ssl.trustStore=/app/jks/store-copel.jks", "-Djavax.net.ssl.trustStorePassword=copel123", "-jar", "app.jar"]
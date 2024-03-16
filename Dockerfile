# Stage 1 : Build the application
FROM maven:3.9.6-amazoncorretto-21 AS build

# Set the working directory in the Maven container
WORKDIR /build

# Copy the pom.xml and source code into the container
COPY pom.xml /build/
COPY src /build/src/

# Package the application without running tests
RUN mvn clean package -DskipTests -Dquarkus.container-image.build=false -Dquarkus.container-image.push=false

# Stage 2 : Set up the runtime container
FROM registry.access.redhat.com/ubi8/openjdk-21:1.18

# Set the language environment variable
ENV LANGUAGE='en_US:en'

# Set the working directory in the runtime container
WORKDIR /deployments

# Copy over the built application from the build stage
COPY --from=build /build/target/quarkus-app/lib/ /deployments/lib/
COPY --from=build /build/target/quarkus-app/*.jar /deployments/
COPY --from=build /build/target/quarkus-app/app/ /deployments/app/
COPY --from=build /build/target/quarkus-app/quarkus/ /deployments/quarkus/

# Expose the HTTP port
EXPOSE 3000

# Use the user 185 for the application (standard for Red Hat's image)
USER 185

# Configure Java options to be appended
ENV JAVA_OPTS_APPEND="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"

# Define the main application jar to run
ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"

# Set the default command to run the application
ENTRYPOINT [ "/opt/jboss/container/java/run/run-java.sh" ]
FROM eclipse-temurin:21-jdk-alpine

VOLUME /tmp

COPY applications/app-service/build/libs/JavaReactivo.jar app.jar

# Opciones JAVA seguras
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"

EXPOSE 8080

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=70", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]

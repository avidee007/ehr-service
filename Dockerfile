FROM eclipse-temurin:17-jdk-jammy
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} ehr-app.jar
ENTRYPOINT ["java","-jar","ehr-app.jar"]
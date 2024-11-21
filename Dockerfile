FROM openjdk:17-oracle
WORKDIR /app
COPY target/test_task-0.0.1-SNAPSHOT.jar test_task.jar
CMD ["java", "-jar", "test_task.jar"]

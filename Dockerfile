FROM adoptopenjdk/openjdk11:alpine-jre
COPY build/libs/*.jar application.jar
ENTRYPOINT ["java","-jar","application.jar"]
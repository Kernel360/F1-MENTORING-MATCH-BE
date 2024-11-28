FROM dkmcw/selenium-java17

ARG PROFILES

COPY ./build/libs/user-api-0.0.1-SNAPSHOT.jar biengual/user-app.jar
COPY ./src/main/resources/.env biengual/.env

EXPOSE 8080

WORKDIR /biengual
ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-Dspring.profiles.active=${PROFILES}", "-jar", "user-app.jar"]
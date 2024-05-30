FROM gradle:jdk21 as builder

WORKDIR /app

COPY ./gradle /app/gradle

COPY ./src /app/src

COPY build.gradle /app/

COPY gradlew /app/

COPY settings.gradle /app/

RUN ["gradle", "--no-daemon", "build"]


FROM openjdk:21

WORKDIR /app

COPY --from=builder /app/build/libs/taxi-app-v1.jar ./

ENTRYPOINT ["java", "-jar", "/app/taxi-app-v1.jar"]

FROM openjdk:8-jdk

COPY . /src
WORKDIR /src

CMD ["./gradlew", "bootRun"]

FROM lapierre/java-alpine:17 AS builder
WORKDIR source
ARG JAR_FILE
COPY ${JAR_FILE} app.jar


FROM lapierre/java-alpine:17

EXPOSE 9011

WORKDIR application
COPY --from=builder source/app.jar ./app.jar

RUN addgroup -g 10001 -S app && adduser -u 10001 -S -G app app

USER 10001

ENTRYPOINT ["java", "-jar", "app.jar"]

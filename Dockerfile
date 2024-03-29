FROM openjdk:8-jdk-alpine
#TimeZone 보정
RUN apk add tzdata
RUN cp /usr/share/zoneinfo/Asia/Seoul /etc/localtime
RUN echo "Asia/Seoul" > /etc/timezone

VOLUME /tmp
ARG JAR_FILE
COPY ./target/Toppos-1.0.0.jar app.jar

ENTRYPOINT ["java","-Djava.net.preferIPv4Stack=true -Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
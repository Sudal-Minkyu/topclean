FROM openjdk:8-jdk-alpine
ENV TZ=Asia/Seoul
RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/$TZ /etc/localtime && \
    echo $TZ > /etc/timezone
VOLUME /tmp
ARG JAR_FILE
COPY ./target/Toppos-1.0.0.jar app.jar

ENTRYPOINT ["java","-Djava.net.preferIPv4Stack=true -Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
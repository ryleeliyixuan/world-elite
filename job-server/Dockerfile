FROM openjdk:8

ENV TZ=Asia/Shanghai
ENV JAVA_OPTS="-Xms256m -Xmx512m -Djava.security.egd=file:/dev/./urandom"

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir -p /apps/worldelite/job-server

WORKDIR /apps/worldelite/job-server

EXPOSE 9000

ADD ./build/libs/*.jar ./
ADD ./build/resources/main/*.properties ./
ADD ./build/resources/main/*.xml ./
ADD ./lexicon .././
ADD ./GeoLite2-City.mmdb ../GeoLite2-City.mmdb

CMD sleep 120;java $JAVA_OPTS -jar job-0.0.1-SNAPSHOT.jar

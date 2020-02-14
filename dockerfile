FROM openjdk:11-jre-slim
WORKDIR /srv

ENV VERSION=0.0.1

COPY ./build/libs/surwave-${VERSION}.jar surwave-${VERSION}.jar
CMD java -XX:+UseG1GC -Xms128m -Xmx128m -Dspring.profiles.active=${CORE_PROFILES} -jar surwave-${VERSION}.jar

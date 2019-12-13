FROM openjdk:11-jdk-slim
WORKDIR /srv
COPY ./build/libs/surwave-0.0.1-SNAPSHOT.jar surwave-0.0.1-SNAPSHOT.jar
CMD java -XX:+UseG1GC -Xms128m -Xmx128m -jar surwave-0.0.1-SNAPSHOT.jar
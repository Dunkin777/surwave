FROM openjdk:11-jre-slim
WORKDIR /srv
COPY ./build/libs/surwave-0.0.1.jar surwave-0.0.1.jar
CMD java -XX:+UseG1GC -Xms128m -Xmx128m -jar surwave-0.0.1.jar
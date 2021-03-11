#FROM adoptopenjdk/openjdk11:latest

#COPY target/*.jar /app.jar

#CMD java -jar /app.jar

FROM adoptopenjdk/openjdk11:latest

WORKDIR /service

COPY target/*.jar service.jar

EXPOSE 8088

ENTRYPOINT exec java -server \
-noverify \
-XX:TieredStopAtLevel=1 \
-Dspring.jmx.enabled=false \
$JAVA_OPTS \
-jar service.jar
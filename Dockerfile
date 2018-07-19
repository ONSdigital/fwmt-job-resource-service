FROM openjdk:8u121-jre
MAINTAINER Kieran Wardle <kieran.wardle@ons.gov.uk>
ARG jar
VOLUME /tmp
COPY $jar resourcesvc.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java -jar /resourcesvc.jar" ]

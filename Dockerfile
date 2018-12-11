FROM java:8
MAINTAINER Maksim Nikanorych (zerosetup@gmail.com)
RUN rm /etc/localtime
RUN ln -s /usr/share/zoneinfo/Europe/Vilnius  /etc/localtime
RUN echo "Europe/Vilnius" > /etc/timezone
COPY target/vaadin-spring-billet-1.0.0-BUILD-SNAPSHOT.jar /vaadin-spring-billet-1.0.0-BUILD-SNAPSHOT.jar
ENTRYPOINT  java -jar /vaadin-spring-billet-1.0.0-BUILD-SNAPSHOT.jar

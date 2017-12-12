FROM openjdk:8u92-jre-alpine
WORKDIR /axon
EXPOSE 8888
EXPOSE 27017
ADD ./build/libs/axon-0.0.1-SNAPSHOT.jar axon.jar
CMD java -jar axon.jar
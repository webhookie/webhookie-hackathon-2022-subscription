FROM openjdk:11.0.12-jdk
MAINTAINER Arthur Kazemi "bidadh@gmail.com"
ARG JAR_FILE=build/libs/webhookie-subscription-sample-0.0.1.jar
ADD ${JAR_FILE} app.jar
EXPOSE 9000
ENTRYPOINT ["java","-jar","/app.jar"]

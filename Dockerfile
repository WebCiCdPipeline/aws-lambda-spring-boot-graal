FROM ghcr.io/graalvm/graalvm-ce:java11-21.1.0
RUN gu install native-image
#RUN mkdir target
#COPY target/* /target
RUN mkdir -p "/opt/app/world/.mvn/wrapper"
COPY ./world /opt/app/world
COPY world/.mvn/wrapper/maven-wrapper.jar /opt/app//world/.mvn/wrapper/maven-wrapper.jar
COPY world/.mvn/wrapper/maven-wrapper.properties /opt/app//world/.mvn/wrapper/maven-wrapper.properties
COPY world/.mvn/wrapper/MavenWrapperDownloader.java /opt/app/world/.mvn/wrapper/MavenWrapperDownloader.java
RUN chmod -R 777 /opt/app/world
RUN mkdir /opt/app/mavendeps
RUN chmod -R 777 /opt/app/mavendeps
RUN cd /opt/app/world && ./mvnw -Pnative -DskipTests clean package
RUN chmod -R 777 /opt/app/world/target
#Copy the /opt/app/world/target/world file to a s3 bucket or a repository and then create
#a new docker image with that file.
#CMD ["/opt/app/world/target/world"]

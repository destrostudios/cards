FROM eclipse-temurin:17-jre-alpine
WORKDIR /home
COPY target/backend-application-0.0.1-jar-with-dependencies.jar ./
COPY assets assets
RUN echo ./assets/ > assets.ini
ARG DB_ROOT_PASSWORD
RUN echo $'//db:3306\nroot\n$DB_ROOT_PASSWORD' > database.ini
ENTRYPOINT ["java", "-jar", "backend-application-0.0.1-jar-with-dependencies.jar"]
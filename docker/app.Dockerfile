FROM eclipse-temurin:17-jre-alpine
WORKDIR /home
COPY target/backend-application-0.0.1-jar-with-dependencies.jar ./
COPY assets assets
ARG DB_ROOT_PASSWORD
RUN echo //db:3306/cards > database.ini && \
    echo root >> database.ini && \
    echo -n $DB_ROOT_PASSWORD >> database.ini && \
    echo -n ./assets/ > assets.ini
ENTRYPOINT ["java", "-jar", "backend-application-0.0.1-jar-with-dependencies.jar"]
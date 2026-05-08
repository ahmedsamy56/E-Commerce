
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B


COPY src ./src
RUN mvn clean package -DskipTests -B


FROM tomcat:10.1-jdk17-temurin

RUN rm -rf /usr/local/tomcat/webapps/*


COPY --from=builder /app/target/E-Commerce-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war


EXPOSE 8080

CMD ["catalina.sh", "run"]

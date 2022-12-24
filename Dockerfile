FROM eclipse-temurin:17-jre-alpine
COPY build/libs/company_prices_api-0.0.1.jar company_prices_api-0.0.1.jar
ENTRYPOINT ["java","-jar","./company_prices_api-0.0.1.jar"]
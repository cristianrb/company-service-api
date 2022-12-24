# Company prices API
Simple API to get prices from different companies (at the moment only implemented: Microsoft, Google and Amazon). 
Values are completely mocked, and they are refreshed every 2 seconds.

## Endpoints
GET /companies -> will return available companies to get list of prices.

GET /companies/{companyName}?timeSeries=time -> will return the prices for a given company in different time series which can be (hourly, daily or weekly)

## Architecture
 - Java 17
 - Spring Boot

## How to run locally
`./gradlew bootRun`

## How to run with docker
`docker compose up -d`

## Postman collection
In the root you will be able to find the Postman collection with name: `NN99.postman_collection` that will allow you to easily test the API when running locally.

Feel free to use different companies and timeseries.
# Sign-in service

## Description
Sign-in service has 2 endpoints which allow sending and checking otp.<br />
It uses a database in memory (H2);<br /> For sending emails uses sendgrid library; <br />
Service has swagger-ui definition endpoints.<br />

## Available endpoints

- `POST http://localhost:8088/sign-in` - send otp
- `POST http://localhost:8088/sign-in/otp` - check otp
- `GET  http://localhost:8088/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config` - swagger
- `GET  http://localhost:8081/sign-in-service/actuator/health` - for health checking
- `GET  http://localhost:8081/sign-in-service/actuator/info` - with some service info

Request samples can be found [here](src/test/java/com/backend/service/sign/in/client/http/api-test.http).

## Configuration

Application parameters can be configured with environment variables

## Environment variables
name|default value|description
   ----|-------|---------
APPLICATION_OTP_LENGTH|4|Length of otp, default 4 digits
APPLICATION_OTP_TTL|180s|Duration of otp, default 3 minutes 
APPLICATION_EMAIL_API_KEY|<apiKey>|Api key which needs for authentication when calling sendgrid endpoint
APPLICATION_EMAIL_SUBJECT|Verification code|Email subject
APPLICATION_EMAIL_FROM_EMAIL|v********@***.net|Sender's email
APPLICATION_EMAIL_FROM_NAME|Vol****** V*****ov|Sender's name

## Test project
```
mvn test
```

## Build project
```
mvn clean package -DskipTests
```

## Run project
```
java -jar target/*.jar
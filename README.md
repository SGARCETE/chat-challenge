# Asapp Chat Challenge - Spring Boot
![technology Java](https://img.shields.io/badge/technology-java-blue.svg)
![tag jdk8](https://img.shields.io/badge/tag-jdk8-orange.svg)

Completed as part of the coding challenge for ASAPP. https://www.asapp.com/

## NOTES

I could achieve implement the following features:

- User creation
- Login
- Message send
- Get messages 
- Token security implementation and password encoding.

For time purposes, could'nt achieve the following features: 

- Application runs in a h2 memory database instead of using Sqlite.
- Messages only can be text.
- There are some todo's comments in the code. They point out the refactors that were not made

## Required pre-requisites:
    Maven
    Java JDK 1.8

## How to run the project?
Run the following commands in order:

1. Clone repo from Github
2. From terminal, in project root, run command **mvn clean package**
3. Run command **sudo docker build --tag=chat-challenge:latest .**
4. Run command **sudo docker run -d -p 8080:8080 -t chat-challenge:latest**

Project will run in localhost:8080. Once the application is running, the stored data in the database can be consulted
in `http://localhost:8080/h2-console` with the following credentials:

```
User Name: sa 
Password: password
```

**IMPORTANT:** Once the application stops, the stored data in database will we delete because H2 is a database that runs in memory

## Database Schema

![Database Schema](https://github.com/sgarcete/chat-challenge/src/main/resources/files/schema.png)

## Api Usage

All the endpoints and request examples are in this public postman collection:

[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/2d8fded996f9976e9024?action=collection%2Fimport)

### Check health

`POST` localhost:8080/health

##### Response
**Status**: `200 OK`
```json
{
    "health": "ok"
}
```

### Create User

- Creates an user with a user_name and a password. User data is saved in db in an encrypted way.
- If user already exists throws a user already exists custom bad request error.
- Returns an autogenerated id that is related to the user.

`POST` localhost:8080/users

`BODY`
```json
{
  "user_name": "user123",
  "password": "user123"
}
```

##### Response
**Status**: `200 OK`
```json
{
  "id": 1
}
```

#### Error Response Format
**Status**: `400 Bad request`
```json
{
  "error": "User already exists",
  "message": "The user with user name user123 does not exists",
  "status": 404
}
```

**Status**: `400 Bad Request`
```json
{
  "message": "Method Argument Not Valid",
  "errors": [
    "Please provide a user_name attribute in JSON request"
  ],
  "status": 400
}
```

### Auth user

- Authenticate an existing user. user_name and password must coincide with the user stored data.
- Returns the user id and a JWT generated session token.
- If user does not exist, returns a custom user does not exist not found error.
- If user information does not coincide with the user stored data, returns custom bad request password invalid exception.

`POST` http://localhost:8080/login

`BODY`
```json
{
  "user_name": "user123",
  "password": "user123"
}
```

#### Success Response
**Status**: `200 OK`
```json
{
  "id": 2,
  "token": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJDaGFsbGVuZ2VKV1QiLCJzdWIiOiJzYW50aTEiLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNjI2Nzg4NjE4LCJleHAiOjE2MjY3ODkyMTh9.Zw-eixgdxqu58Zs-MYLlUY5UHwzF_eXSAHs4ruItN32jmEIa0zeFF4Afh6M2R4yb9EqxUBlRiQGKJrWuGmNdhA"
}
```
#### Error Response Format

**Status**: `400 Bad Request`
```json
{
  "message": "Method Argument Not Valid",
  "errors": [
    "Please provide a user_name attribute in JSON request"
  ],
  "status": 400
}
```
**Status**: `404 Not found`
```json
{
  "error": "User does not exist",
  "message": "The user with user name user123 does not exists",
  "status": 404
}
```

**Status**: `400 Bad request`
```json
{
  "error": "Password not valid exception",
  "message": "Password not valid for user user123",
  "status": 400
}
```

### Send Messages

- Send a message from a sender user to a recipient user.
- If one of the users does not exist, returns a custom exception.
- Returns the message id and the timestamp.
- It requires an authorization header with the provided token in login post.

`POST` http://localhost:8080/messages

`HEADERS`
```json
"Authorization":"Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJDaGFsbGVuZ2VKV1QiLCJzdWIiOiJzYW50aTEiLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNjI2Nzg4NjE4LCJleHAiOjE2MjY3ODkyMTh9.Zw-eixgdxqu58Zs-MYLlUY5UHwzF_eXSAHs4ruItN32jmEIa0zeFF4Afh6M2R4yb9EqxUBlRiQGKJrWuGmNdhA"
```

`BODY`
```json
{
  "sender": 1,
  "recipient": 2,
  "content": {
    "type": "STRING",
    "text": "abc"
  }
}
```

#### Success Response
**Status**: `200 OK`
```
{
    "id": 4,
    "timestamp": "2021-07-20T10:44:27.825-03:00"
}
```
#### Error Response Format

**Status**: `400 Bad Request`
```json
{
  "error": "Message not readable exception",
  "message": "Content type must be STRING, VIDEO or IMAGE",
  "status": 400
}
```

**Status**: `404 Not found`
```json
{
  "error": "User does not exist",
  "message": "The user with id 2 does not exist",
  "status": 404
}
```

**Status**: `403 fordidden`
```json
{
  "error": "Forbidden",
  "message": "",
  "status": 403
}
```

### Get messages

- Gets all the messages that a recipient users received.
- It requires an authorization header with the provided token in login post.
- If user doest not exist, returns a custom not found error.

`GET` http://localhost:8080/messages?recipient=2&start=2&limit=3

`HEADERS`
```json
"Authorization":"Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJDaGFsbGVuZ2VKV1QiLCJzdWIiOiJzYW50aTEiLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNjI2Nzg4NjE4LCJleHAiOjE2MjY3ODkyMTh9.Zw-eixgdxqu58Zs-MYLlUY5UHwzF_eXSAHs4ruItN32jmEIa0zeFF4Afh6M2R4yb9EqxUBlRiQGKJrWuGmNdhA"
```

#### Success Response
**Status**: `200 OK`
```json
{
  "messages": [
    {
      "id": 1,
      "content": {
        "type": "STRING",
        "text": "abc"
      },
      "timestamp": "2021-07-20T16:06:29.95-03:00",
      "sender": 1,
      "recipient": 2
    },
    {
      "id": 2,
      "content": {
        "type": "STRING",
        "text": "abc"
      },
      "timestamp": "2021-07-20T16:06:30.933-03:00",
      "sender": 1,
      "recipient": 2
    }
  ]
}
```

**Status**: `404 Not found`
```json
{
  "error": "User does not exist",
  "message": "The user with id 2 does not exist",
  "status": 404
}
```

**Status**: `403 fordidden`
```json
{
  "error": "Forbidden",
  "message": "",
  "status": 403
}
```

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.4.3/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.4.3/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.4.3/reference/htmlsingle/#boot-features-developing-web-applications)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.4.3/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [Spring Security](https://docs.spring.io/spring-security/site/docs/current/reference/html5/)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

## Questions
* [santi101093@gmail.com](mailto:santi101093@gmail.com)


# RISE-User-Management-2.0

# Index

- [Why 2.0](#why-20)
- [What](what)
- [Useful documentation](#useful-documentation)
- [OAuth2.0 in our System](#oauth20-in-our-system)
- [Setup and Run](#setup-and-run)
- [Future TODOs](#future-todos)
- [Contributions](#contributions)

## Why 2.0 :question:

Our goal is to make our microservice (from here ‘MS’) more machine-independent. The [previous version](https://github.com/RISE-Remote-Intranet-School-Environment/RISE-User-management) was built in NodeJS and Wamp. The latter runs only on Windows.

We will re-build the system with Java using the very popular (and well documented) framework Spring.

Additionally, we want to do it providing proper Javadoc (which was completely missing before) and Unit tests.

## What :collision:

The MS should implement the OAuth2 authentication protocol to other MSs. The pattern can be described as follows:

[]img

## Useful documentation :page_with_curl:

Spring security documentation for oauth2 login:
https://docs.spring.io/spring-security/reference/servlet/oauth2/login/core.html

Spring Boot and OAuth2 tutorial:
https://spring.io/guides/tutorials/spring-boot-oauth2/

## Oauth 2.0 and OpenId Connect protocols

Quick explanation goes here..

### Roles

An OAuth 2.0 flow has the following roles:

- Resource Owner: Entity that can grant access to a protected resource. Typically, this is the end-user.

- Resource Server: Server hosting the protected resources. This is the API you want to access.

- Client: Application requesting access to a protected resource on behalf of the Resource Owner.

- Authorization Server: Server that authenticates the Resource Owner and issues access tokens after getting proper authorization. In this case, Auth0.

## Oauth 2.0 in our system

Documentation from Spring tutorial goes here

## Setup and Run

- Requirements: maven, jdk
- Clone the repository
- Open it with VSCode or IntelliJ
- Get your client id and secret from GitHub: go [here](#https://github.com/settings/developers), select "New OAuth App" and then the "Register a new OAuth application" page is presented. Enter an app name and description. Then, enter your app’s home page, which should be http://localhost:8080, in this case. Finally, indicate the Authorization callback URL as http://localhost:8080/login/oauth2/code/github and click Register Application.
- Get your client id and secret from Google: go [here](#https://developers.google.com/identity/protocols/OpenIDConnect), follow the instructions on the OpenID Connect page, starting in the section, "Setting up OAuth 2.0". After completing the "Obtain OAuth 2.0 credentials" instructions, you should have a new OAuth Client with credentials consisting of a Client ID and a Client Secret.
- The application.yml is not tracked for security reasons. Add your own manually or with: `touch src/main/resources/application.yml`
- Then you can modify it using this template:
```yml
spring:
  security:
    oauth2:
      client:
        registration:
          github:
            clientId: <your-client-id>
            clientSecret: <your-client-secret>
          google:
            client-id: <your-client-id>
            client-secret: <your-client-secret>
```
- After doing that, you can boot the application with: `mvn spring-boot:run`
- open your browser at `http://localhost:8080`
- Test the login and logout

## Contributions

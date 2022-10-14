# RISE-User-Management-2.0

# Index

- why
- What
- Useful documentation
- OAuth2.0 in our System
- Setup and Run
- Future TODOs
- Contributions

## Why?

Our goal is to make our microservice (from here ‘MS’ more machine-independent. The previous version was built in NodeJS and Wamp. The latter runs only on Windows.

We will re-build the system with Java using the very popular (and well documented) framework Spring.

Additionally, we want to do it providing proper Javadoc (which was completely missing before) and Unit tests.

## What?

The MS should implement the OAuth2 authentication protocol to other MSs. The pattern can be described as follows:

[]img

## Useful documentation

Spring security documentation for oauth2 login
https://docs.spring.io/spring-security/reference/servlet/oauth2/login/core.html

## Oauth 2.0 in our system

### Roles

An OAuth 2.0 flow has the following roles:

- Resource Owner: Entity that can grant access to a protected resource. Typically, this is the end-user.

- Resource Server: Server hosting the protected resources. This is the API you want to access.

- Client: Application requesting access to a protected resource on behalf of the Resource Owner.

- Authorization Server: Server that authenticates the Resource Owner and issues access tokens after getting proper authorization. In this case, Auth0.

## Setup and Run

## Contributions

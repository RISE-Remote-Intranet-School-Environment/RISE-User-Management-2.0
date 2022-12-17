# RISE-User-Management-2.0

# Index

- [Why 2.0](#why-20-question)
- [What](#what-collision)
- [Useful documentation](#useful-documentation-page_with_curl)
- [Oauth 2.0 and OpenId Connect protocols](#oauth-20-and-openid-connect-protocols)
- [Setup and Run](#setup-and-run)
- [Future TODOs](#future-todos)
- [Contributions](#contributions)

## Why 2.0 :question:

Our goal is to make our microservice (from here ‘MS’) more machine-independent. The [previous version](https://github.com/RISE-Remote-Intranet-School-Environment/RISE-User-management) was built in NodeJS and Wamp. The latter runs only on Windows.

We will re-build the system with Java using the very popular (and well documented) framework Spring.

Additionally, we want to do it providing proper Javadoc (which was missing before) and (possibly) Unit tests.

## What :collision:

The MS should implement the OAuth2 authentication protocol to other MSs. The pattern can be described as follows:

!['MS architecure'](./MS%20architecture.png)

The image above describes the architecture as imagined for now. In plain english, we want to use OAuth2 to manage authentication for users (numbers correspond to image).

- (1) Users connect using their Google (and/or Github?) account;
- (2) If a user connects for the first time, they are added to the database (need to setup a check to see if the user is already registered or not);
- The database (a simple mysql db) saves the openid, the email and the role. A new user should receive the default role of guest;
- (3) The admin user should have a small frontend to manage users (i.e. change roles, at this stage, maybe more later)
- (4)(5) Whenever the user tries to access another MS, our MS needs to tell the other which role is assigned to the user so they can display the right page. Ideally, this communication only includes the id of the user and their role. 

The whole point of this MS is to limit the amount of data the other MS have about the users, and to store as little data on the user as we can.

Note: a JWT parser with unit test is implemented in [fill]. It can be used to parse the JWT sent by the authorization server to populate the DB and save their ID for the session.

So far, the proposed **roles** are :
- Admin
- Professor
- Student
- Printer (not a machine, a person with printing privileges)
- Guest (default)

## Useful documentation :page_with_curl:

Spring security documentation for oauth2 login:
https://docs.spring.io/spring-security/reference/servlet/oauth2/login/core.html

Spring Boot and OAuth2 tutorial:
https://spring.io/guides/tutorials/spring-boot-oauth2/

Spring Boot SQL database tutorial:
https://spring.io/guides/gs/accessing-data-mysql/

## Oauth 2.0 and OpenId Connect protocols

What are OAuth2.0 and OpenID Connect?

**Qualitative**

*OAuth2.0* is a standard for authorization. Its main purpose is to allow third-party apps to access certain data from a user, in a secured way, through what is called an authorization server. Some classical examples include images, calendars, and so on.
Official OAuth2.0 doc:
https://www.rfc-editor.org/rfc/rfc6749#section-1.2

*OpenID Connect* is an extension built on OAuth2.0 to allow for authentication. The principles are the same as OAuth, but you can use it to log in. Most "Log in with Google" functions implement OAuth2.0 with OpenID Connect.
Official OpenID Connect doc:
https://openid.net/connect/

Together they serve as a more secure alternative to traditional authentication, leaving the complex security issues to auhentication providers (like Google, Facebook, Twitter, Github...)

**Technical**

First, let's look at *roles* in OAuth 2.0 (! not the same as roles in our MS) :

- Resource Owner: Entity that can grant access to a protected resource. Typically, this is the end-user.

- Resource Server: Server hosting the protected resources. This is the API you want to access.

- Client: Application requesting access to a protected resource on behalf of the Resource Owner.

- Authorization Server: Server that authenticates the Resource Owner and issues access tokens after getting proper authorization. In this case, Auth0.

Next, let's look at the *theoretical flow* :

1. The client sends an authorization request to the resource owner (to use some resource, like a calendar, called "scope"); usually this happens through the authorization server.
2. The resource owner give an authorization grant to the client in the form of credentials.
3. The client sends this grant to the authorization server, asking for an access token. Also, the client authenitcates with the authorization server (client id and client secret).
4. If the client can be authenticate, and the authorization grant is valid, the authorization server replies with an access token.
5. The client request the resources from the resource server, presenting the token.
6. If the token can be validated, the resource server sends the resources.

Finally, let's look at the *real flow*

The previous flow is only conceptual. In reality, things are more complicated.

- Back channel and front channel : the previous flow is used in apps with no backend. This is less secure, because the token could be intercepted (altough this is hard to do). One way to secure this is to add the PCKE 
 

## About Oauth 2.0 with Spring

As you may have understood while reading about the protocol, OAuth2 and OpenID Connect are conceptually easy, but can be very difficult to implement safely. Spring Security helps a lot with this, but it makes the code quite hard to understand. However, it should run fine and *safely*.

At this stage, the main worry should be to manage users within the application.

## Setup and Run

- Requirements: maven, jdk
- Clone the repository
- Open it with VSCode or IntelliJ
- Get your client id and secret from GitHub: go [here](https://github.com/settings/developers), select "New OAuth App" and then the "Register a new OAuth application" page is presented. Enter an app name and description. Then, enter your app’s home page, which should be http://localhost:8080, in this case. Finally, indicate the Authorization callback URL as http://localhost:8080/login/oauth2/code/github and click Register Application.
- Get your client id and secret from Google: go [here](https://developers.google.com/identity/protocols/OpenIDConnect), follow the instructions on the OpenID Connect page, starting in the section, "Setting up OAuth 2.0". After completing the "Obtain OAuth 2.0 credentials" instructions, you should have a new OAuth Client with credentials consisting of a Client ID and a Client Secret.
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
            scope: <scope>
          google:
            client-id: <your-client-id>
            client-secret: <your-client-secret>
            scope: <scope> # example : email, profile, openid
server:
  port: <port number>
```
- After doing that, you can boot the application with: `mvn spring-boot:run`
- open your browser at `http://localhost:8080`
- Test the login and logout

## Future TODOs

- Figure out how to extract the JWT from the return from Google/Github (to be sent to the parser). Hint : go up the principal object in SimpleApplication, it should lead to the return object (this is a hypothesis, not cheked)
- Connect the login app to the database
- Build a frontend for the admin and connect it to the database
- Build an API for the other MS to communicate with this MS
- Connect to db to the database

(see also github project linked to this repo)

## Contributions

- [Fatjon Freskina](https://github.com/fatjonfreskina)
- Denis Rygaert
- Hadrien Godts
- Louise Boulard

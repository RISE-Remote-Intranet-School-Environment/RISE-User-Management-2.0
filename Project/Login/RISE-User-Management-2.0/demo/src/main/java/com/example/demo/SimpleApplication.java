package com.example.demo;

// for unused imports, see commented code below
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@RestController
public class SimpleApplication extends WebSecurityConfigurerAdapter {

    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("name", principal.getAttribute("name"));
    }

    @GetMapping("/error")
    public String error(HttpServletRequest request) {
        String message = (String) request.getSession().getAttribute("error.message");
        request.getSession().removeAttribute("error.message");
        return message;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .authorizeRequests(a -> a
                .antMatchers("/", "/error", "/webjars/**").permitAll()
                .anyRequest().authenticated()
            )
            .exceptionHandling(e -> e
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            ).logout(l -> l
            .logoutSuccessUrl("/").permitAll()
            ).csrf(c -> c
		    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
	        )
            .oauth2Login();
        // @formatter:on
    }

    // The code below adds errors for unauthenticated users, doesn't work for github for some reason

    /*
    @Bean
    public WebClient rest(ClientRegistrationRepository clients, OAuth2AuthorizedClientRepository authz) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 = new ServletOAuth2AuthorizedClientExchangeFilterFunction(
                clients, authz);
        return WebClient.builder()
                .filter(oauth2).build();
    }

    private Consumer<Map<String, Object>> oauth2AuthorizedClient(OAuth2AuthorizedClient client) {
        return null;
    }
 
    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService(WebClient rest) {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        return request -> {
            OAuth2User user = delegate.loadUser(request);
            if (!"github".equals(request.getClientRegistration().getRegistrationId())) {
                return user;
            }

            OAuth2AuthorizedClient client = new OAuth2AuthorizedClient(request.getClientRegistration(), user.getName(),
                    request.getAccessToken());
            String url = user.getAttribute("organizations_url");
            List<Map<String, Object>> orgs = rest
                    .get().uri(url)
                    .attributes(ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient(client))
                    .retrieve()
                    .bodyToMono(List.class)
                    .block();

            if (orgs.stream().anyMatch(org -> "spring-projects".equals(org.get("login")))) {
                return user;
            }

            throw new OAuth2AuthenticationException(new OAuth2Error("invalid_token", "Not in ECAM", ""));
        };
    }
*/
    public static void main(String[] args) {
        SpringApplication.run(SimpleApplication.class, args);
    }
    
    
    // !!! CODE BELOW NOT WORKING - uses the "unused imports" above !!! 
    // This part of the code is meant to communicate with the database and was used to test things out.
    // It needs to be able to compare the email of a user that is connecting with the database to see if the user already exists.
    // If that user doesn't exist in the database, he/she is written to it with the openid, email and default role "GUEST".
    // Changing the role is done through a controller in the DB app (see repo)
    // The code below is meant to communicate with the DB (see repo) through http requests : This may not be the most optimal 
    // solution, so tread carefuly. Everything written here is to send hardcoded data to the database; eventually it's the data
    // from the JWT that will need to be sent.
     
    /*
    public class HttpURLConnectionExample {
    
        private static final String GET_URL = "https://localhost:8081/demo/all";
    
        private static final String POST_URL = "https://localhost:8081/demo/add";
    
        private static final String POST_PARAMS_1 = "email=test@ecam.be?role=GUEST";
    
        public static void main(String[] args) throws IOException {

            sendPOST();
            System.out.println("POST DONE");
        }
        
        private static void sendGET() throws IOException {
            URL obj = new URL(GET_URL);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            System.out.println("GET Response Code :: " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
    
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
    
                // print result
                System.out.println(response.toString());
            } else {
                System.out.println("GET request did not work.");
            }
    
        } 
    
        private static void sendPOST() throws IOException {
            URL obj = new URL(POST_URL);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
    
            // For POST only - START
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            os.write(POST_PARAMS_1.getBytes());
            os.flush();
            os.close();
            // For POST only - END
    
            int responseCode = con.getResponseCode();
            System.out.println("POST Response Code :: " + responseCode);
    
            if (responseCode == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
    
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
    
                // print result
                System.out.println(response.toString());
            } else {
                System.out.println("POST request did not work.");
            }
        }
    } */   
}

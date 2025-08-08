package com.expensetracker.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import com.expensetracker.auth.service.CustomOAuth2UserService;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private CustomOAuth2UserService customOAuth2UserService;

	

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
              //  .requestMatchers("/", "/login**", "/error**", "/css/**", "/js/**", "/static/**").permitAll()
            .authorizeHttpRequests(authz -> authz
            	    .requestMatchers("/", "/login", "/error").permitAll()
            	    .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
            	    .requestMatchers("/user/**").hasAuthority("ROLE_USER")
            	    .requestMatchers("/expenses/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
            	    .requestMatchers("/home/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
            	    .requestMatchers("/dashboard/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
            	    .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
            	    .loginPage("/login")
            	    .defaultSuccessUrl("/dashboard", true)
            	    .failureUrl("/login?error=true")
            	    .userInfoEndpoint(userInfo -> 
            	     	userInfo.userService(customOAuth2UserService)  // use our custom service
            	    )
            )
            .logout(logout -> logout
            	    .logoutSuccessUrl("/")
            	    .invalidateHttpSession(true)
            	    .clearAuthentication(true)
            	    .deleteCookies("JSESSIONID")
            	    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
            	);
        return http.build();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.googleClientRegistration());
    }

    private ClientRegistration googleClientRegistration() {
        return ClientRegistration.withRegistrationId("google")
            //.clientId()
            //.clientSecret()
            .scope("profile", "email") // Remove "openid" to avoid OIDC JWT validation
            .userNameAttributeName("email")  // use email as the username/principal
            .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
            .tokenUri("https://oauth2.googleapis.com/token")
            .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
            .clientName("Google")
            .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            // Remove jwkSetUri to avoid JWT validation issues
            .build();
    }

    @Bean
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
        return new HandlerMappingIntrospector();
    }
}
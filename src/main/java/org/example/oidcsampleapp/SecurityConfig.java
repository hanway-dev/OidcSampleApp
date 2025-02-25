package org.example.oidcsampleapp;

import org.example.oidcsampleapp.Services.HUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final HUserService hUserService;

    public SecurityConfig(HUserService hUserService) {
        this.hUserService = hUserService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/oauth2/authorization/oidc", "/callback", "/logout_callback", "/error").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(a -> a
                                .baseUri("/oauth2/authorization")
                        )
                        .redirectionEndpoint(r -> r
                                .baseUri("/callback")
                        )
                        .userInfoEndpoint(userInfo -> userInfo
                                .oidcUserService(this.hUserService)
                        )
                        .defaultSuccessUrl("/", true)
                )
                .logout(l -> l
                        .logoutSuccessUrl("/")
                        .logoutUrl("/logout")
                        .addLogoutHandler((request, response, authentication) -> {
                            OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
                            String idToken = oidcUser.getIdToken().getTokenValue();

                            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
                            String logoutUrl = "{IDP_END_SESSION_URL}" +
                                    "id_token_hint=" + idToken + "&" +
                                    "post_logout_redirect_uri=" + baseUrl + "/logout_callback";
                            try {
                                response.sendRedirect(logoutUrl);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                );

        return http.build();
    }
}

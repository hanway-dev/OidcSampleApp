package org.example.oidcsampleapp.Services;

import org.example.oidcsampleapp.Models.HUser;
import org.example.oidcsampleapp.Repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class HUserService extends OidcUserService {
    private final UserRepository userRepository;

    public HUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) {
        var oidcUser = super.loadUser(userRequest);

        var email = oidcUser.getEmail();
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new OAuth2AuthenticationException(new OAuth2Error("invalid email", "Can't find user with email", null)));

        return oidcUser;
    }

    public HUser getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof OidcUser) {
            var oidcUser = (OidcUser) authentication.getPrincipal();
            return userRepository.findByEmail(oidcUser.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }
        throw new RuntimeException("Not authenticated");
    }
}

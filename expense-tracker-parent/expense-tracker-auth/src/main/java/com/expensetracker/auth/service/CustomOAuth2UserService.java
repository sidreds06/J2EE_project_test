package com.expensetracker.auth.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.expensetracker.auth.entity.AuthUser;
import com.expensetracker.auth.repository.AuthUserRepository;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private AuthUserRepository userRepo;

    private final DefaultOAuth2UserService defaultOAuth2UserService;

    public CustomOAuth2UserService() {
        this.defaultOAuth2UserService = new DefaultOAuth2UserService();
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        if (email == null) {
            throw new OAuth2AuthenticationException("Email not found from Google authentication");
        }

        AuthUser user = userRepo.findByEmail(email);
        if (user == null) {
            user = new AuthUser();
            user.setEmail(email);
            user.setName(name != null ? name : email);
            user.setRoles("ROLE_USER"); // default role
            userRepo.save(user);
        } else {
            if (name != null && !name.equals(user.getName())) {
                user.setName(name);
                userRepo.save(user);
            }
        }

        // Convert roles string (e.g., "USER,ADMIN") into GrantedAuthority list
        List<GrantedAuthority> authorities = List.of(user.getRoles().split(",")).stream()
                .map(String::trim)
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());

        return new DefaultOAuth2User(
                authorities,
                oAuth2User.getAttributes(),
                "email"
        );
    }
}

package com.debttrackr.security;

import com.debttrackr.domain.User;
import com.debttrackr.domain.enumeration.AuthProvider;
import com.debttrackr.domain.enumeration.Role;
import com.debttrackr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService
        extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(
            OAuth2UserRequest request)
            throws OAuth2AuthenticationException {

        OAuth2User oauthUser =
                super.loadUser(request);

        String email = oauthUser.getAttribute("email");

        String name = oauthUser.getAttribute("name");

        String picture = oauthUser.getAttribute("picture");

        userRepository.findByEmail(email)

                .orElseGet(() -> {
                    User user = new User();
                    user.setName(name);
                    user.setEmail(email);
                    user.setPicture(picture);
                    user.setRole(Role.USER);
                    user.setProvider(AuthProvider.GOOGLE);
                    user.setCreatedDate(new java.sql.Date(System.currentTimeMillis()));
                    return userRepository.save(user);
                });

        return oauthUser;
    }

}

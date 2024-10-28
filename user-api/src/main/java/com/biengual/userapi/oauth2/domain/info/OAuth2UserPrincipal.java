package com.biengual.userapi.oauth2.domain.info;

import com.biengual.userapi.user.domain.UserEntity;
import com.biengual.userapi.user.domain.enums.Role;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
@Builder
public class OAuth2UserPrincipal implements OAuth2User {
    private Long id;
    private String email;
    private String username;
    private String provider;
    private String providerId;
    private Role role;

    // OAuth2User Override Method
    @Override
    public String getName() {
        return this.email;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.role == null ? null : Collections.singleton(new SimpleGrantedAuthority(this.role.name()));
    }

    public static OAuth2UserPrincipal from(OAuth2UserInfo oAuth2UserInfo) {
        return OAuth2UserPrincipal.builder()
            .email(oAuth2UserInfo.getEmail())
            .username(oAuth2UserInfo.getUsername())
            .provider(oAuth2UserInfo.getProvider())
            .providerId(oAuth2UserInfo.getProviderId())
            .build();
    }

    public static OAuth2UserPrincipal from(UserEntity user) {
        return OAuth2UserPrincipal.builder()
            .id(user.getId())
            .email(user.getEmail())
            .username(user.getUsername())
            .provider(user.getProvider())
            .providerId(user.getProviderId())
            .role(user.getRole())
            .build();
    }
}

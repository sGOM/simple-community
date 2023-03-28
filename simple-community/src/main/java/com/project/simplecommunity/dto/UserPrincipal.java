package com.project.simplecommunity.dto;

import com.project.simplecommunity.dto.reqeust.UserAccountDto;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public record UserPrincipal(
        String username,
        String password,
        Collection<? extends GrantedAuthority> authorities,
        String email
) implements UserDetails {

    public static UserPrincipal of(String username, String password, String email) {
        // 지금은 인증만 하고 권한을 다루고 있지 않아서 임의로 세팅한다.
        Set<RoleType> roleTypes = Set.of(RoleType.USER);

        return new UserPrincipal(
                username,
                password,
                roleTypes.stream()
                        .map(RoleType::getName)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toUnmodifiableSet()),
                email
        );
    }

    public static UserPrincipal from(UserAccountDto dto) {
        return UserPrincipal.of(
                dto.userId(),
                dto.password(),
                dto.email()
        );
    }

    public UserAccountDto toDto() {
        return UserAccountDto.of(
                username,
                password,
                email
        );
    }


    @Override public String getUsername() { return username; }
    @Override public String getPassword() { return password; }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    public enum RoleType {
        USER("ROLE_USER");

        @Getter
        private final String name;

        RoleType(String name) {
            this.name = name;
        }
    }
}

package mk.ukim.finki.web_seminarska.model.enumerations;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    ROLE_CLIENT,
    ROLE_STAFF;

    @Override
    public String getAuthority() {
        return name();
    }
}

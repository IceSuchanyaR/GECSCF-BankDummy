package gec.scf.dummy.common.utils;

import java.util.Collection;

import gec.scf.dummy.security.model.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public class SecurityUtils {

    public static final String HISTORY = "history";

    public static boolean verify(String scope) {
        try {
            OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder
                    .getContext().getAuthentication();
            if (auth.getOAuth2Request().getScope().contains(scope)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }

        return false;
    }

    public static boolean verifyHasAny(String... authorities) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            for (String authority : authorities) {
                if (auth.getAuthorities()
                        .contains(new SimpleGrantedAuthority(authority))) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }

        return false;
    }

    public static Collection<? extends GrantedAuthority> getAuthorities() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities();
    }

    public static String getClientId() {
        OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder
                .getContext().getAuthentication();
        return authentication.getOAuth2Request().getClientId();

    }

    public static User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}

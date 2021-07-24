package com.safelogisitics.gestionentreprisesusers.security;

import java.io.Serializable;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

  @Override
  public boolean hasPermission(Authentication auth, Object permission, Object action) {
    if ((auth == null) || (permission == null) || (action == null)) {
      return false;
    }
    return hasPrivilege(auth, permission.toString().toUpperCase(), action.toString().toUpperCase());
  }

  @Override
  public boolean hasPermission(Authentication auth, Serializable targetId, String permission, Object action) {
    if ((auth == null) || (permission == null) || (action == null)) {
      return false;
    }
    return hasPrivilege(auth, permission.toUpperCase(), action.toString().toUpperCase());
  }

  private boolean hasPrivilege(Authentication auth, String permission, String action) {
    for (final GrantedAuthority grantedAuth : auth.getAuthorities()) {
      if (grantedAuth.getAuthority().startsWith(permission) && grantedAuth.getAuthority().contains(action.toUpperCase())) {
        return true;
      }
    }
    return false;
  }
}

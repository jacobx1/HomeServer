package com.jacobx1.homeserver.service.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import java.util.*;
import org.dhatim.dropwizard.jwt.cookie.authentication.JwtCookiePrincipal;

public class UserPrincipal implements JwtCookiePrincipal {

  private final Claims userClaims;

  private final Set<String> roles;
  private final String name;
  private final Integer userId;

  public UserPrincipal(Claims claims) {
    userClaims = claims;
    name = claims.getSubject();
    ArrayList<String> rawRoles = claims.get("roles", ArrayList.class);

    roles = new HashSet<String>(rawRoles);
    userId = claims.get("userId", Integer.class);
  }

  public UserPrincipal(int id, String name) {
    this.name = name;
    userClaims = new DefaultClaims();
    userClaims.setSubject(name);
    userClaims.put("userId", id);
    userId = id;
    roles = new HashSet<>();
  }

  @Override
  public boolean isPersistent() {
    return true;
  }

  @Override
  public boolean isInRole(String s) {
    return roles.contains(s);
  }

  @Override
  public String getName() {
    return name;
  }

  public void setRoles(Collection<String> addedRoles) {
    roles.addAll(addedRoles);
  }

  public Claims toClaims() {
    userClaims.put("roles", roles.toArray());
    userClaims.setSubject(name);
    userClaims.put("userId", userId);
    return userClaims;
  }
}

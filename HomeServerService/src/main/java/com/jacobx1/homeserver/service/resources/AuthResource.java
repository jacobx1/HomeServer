package com.jacobx1.homeserver.service.resources;

import com.google.inject.Inject;
import com.jacobx1.homeserver.service.auth.UserPrincipal;
import com.jacobx1.homeserver.service.dao.DataDao;
import com.jacobx1.homeserver.service.managers.UserManager;
import com.jacobx1.homeserver.service.model.UserLoginRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import org.dhatim.dropwizard.jwt.cookie.authentication.DefaultJwtCookiePrincipal;
import org.dhatim.dropwizard.jwt.cookie.authentication.JwtCookiePrincipal;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/auth")
public class AuthResource {

  @Inject
  private UserManager userManager;

  @POST
  @Path("/login")
  public JwtCookiePrincipal login(
    @Context ContainerRequestContext requestContext,
    UserLoginRequest loginRequest
  ) {
    var user = userManager.authenticateUser(
      loginRequest.getUsername(),
      loginRequest.getPassword()
    );
    if (user.isPresent()) {
      UserPrincipal principal = new UserPrincipal(
        user.get().getId(),
        user.get().getName()
      );
      principal.setRoles(List.of(user.get().getRole()));
      principal.addInContext(requestContext);
      return principal;
    }

    throw new BadRequestException();
  }

  @POST
  @Path("/register")
  public void register(UserLoginRequest loginRequest) {
    var user = userManager.registerUser(
      loginRequest.getUsername(),
      loginRequest.getPassword()
    );
    if (user.isEmpty()) {
      throw new BadRequestException();
    }
  }
}

package com.jacobx1.homeserver.service.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.jacobx1.homeserver.service.api.Saying;
import com.jacobx1.homeserver.service.dao.DataDao;
import com.jacobx1.homeserver.service.model.User;
import io.dropwizard.auth.Auth;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import ru.vyarus.dropwizard.guice.module.yaml.bind.Config;

@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldResource {

  @Inject
  @Config("template")
  private String template;

  @Inject
  @Config("defaultName")
  private String defaultName;

  private final AtomicLong counter = new AtomicLong();

  @Inject
  private Provider<HttpServletRequest> requestProvider;

  @Inject
  private DataDao dataDao;

  @GET
  @RolesAllowed("user")
  @Path("/ip")
  public String getIp() {
    return requestProvider.get().getRemoteAddr();
  }

  @GET
  @Timed
  public Saying sayHello(@QueryParam("name") Optional<String> name) {
    final String value = String.format(template, name.orElse(defaultName));
    return new Saying(counter.incrementAndGet(), value);
  }

  @GET
  @Timed
  @Path("/by-id")
  public User byId(@QueryParam("id") int id) {
    return dataDao.findNameForId(id);
  }
}

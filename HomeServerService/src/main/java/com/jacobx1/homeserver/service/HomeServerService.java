package com.jacobx1.homeserver.service;

import com.jacobx1.homeserver.service.auth.UserPrincipal;
import com.jacobx1.homeserver.service.config.HomeServerServiceConfiguration;
import com.jacobx1.homeserver.service.resources.HelloWorldResource;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import org.dhatim.dropwizard.jwt.cookie.authentication.JwtCookieAuthBundle;
import ru.vyarus.dropwizard.guice.GuiceBundle;
import ru.vyarus.guicey.jdbi3.JdbiBundle;

public class HomeServerService extends Application<HomeServerServiceConfiguration> {

  public static void main(String[] args) throws Exception {
    new HomeServerService().run(args);
  }

  @Override
  public String getName() {
    return "hello-world";
  }

  @Override
  public void initialize(Bootstrap<HomeServerServiceConfiguration> bootstrap) {
    // nothing to do yet
    bootstrap.setConfigurationSourceProvider(new ResourceConfigurationSourceProvider());
    bootstrap.addBundle(
      GuiceBundle
        .builder()
        .bundles(
          JdbiBundle.<HomeServerServiceConfiguration>forDatabase((conf, env) ->
            conf.getDataSourceFactory()
          )
        )
        .enableAutoConfig()
        .build()
    );
    bootstrap.addBundle(
      new JwtCookieAuthBundle<HomeServerServiceConfiguration, UserPrincipal>(
        UserPrincipal.class,
        UserPrincipal::toClaims,
        UserPrincipal::new
      )
        .withConfigurationSupplier(HomeServerServiceConfiguration::getJwtCookieAuth)
    );
  }

  @Override
  public void run(HomeServerServiceConfiguration configuration, Environment environment) {
    // nothing to do yet
  }
}

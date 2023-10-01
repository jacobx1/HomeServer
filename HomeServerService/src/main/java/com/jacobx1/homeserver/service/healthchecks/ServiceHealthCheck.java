package com.jacobx1.homeserver.service.healthchecks;

import com.google.inject.Inject;
import com.jacobx1.homeserver.service.HomeServerService;
import ru.vyarus.dropwizard.guice.module.installer.feature.health.NamedHealthCheck;

public class ServiceHealthCheck extends NamedHealthCheck {

  @Inject
  private HomeServerService service;

  @Override
  protected Result check() throws Exception {
    return Result.healthy();
  }

  @Override
  public String getName() {
    return "service-health";
  }
}

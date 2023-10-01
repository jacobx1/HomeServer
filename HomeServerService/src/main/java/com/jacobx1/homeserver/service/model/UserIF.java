package com.jacobx1.homeserver.service.model;

import com.hubspot.immutables.style.HubSpotStyle;
import org.immutables.value.Value;

@HubSpotStyle
@Value.Immutable
public interface UserIF extends UserWithRoleIF {
  Integer getId();
}

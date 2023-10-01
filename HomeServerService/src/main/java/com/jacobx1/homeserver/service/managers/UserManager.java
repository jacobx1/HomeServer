package com.jacobx1.homeserver.service.managers;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCryptParser;
import com.google.inject.Inject;
import com.jacobx1.homeserver.service.dao.DataDao;
import com.jacobx1.homeserver.service.model.User;
import com.jacobx1.homeserver.service.model.UserWithRole;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class UserManager {

  @Inject
  private DataDao dataDao;

  public Optional<User> registerUser(String username, String password) {
    var maybeUser = dataDao.findUserByName(username);
    if (maybeUser.isPresent()) {
      return Optional.empty();
    }

    var hashedPassword = BCrypt.withDefaults().hashToString(6, password.toCharArray());
    var newUser = UserWithRole
      .builder()
      .setName(username)
      .setPassword(hashedPassword)
      .setRole("user")
      .build();
    dataDao.insertUser(newUser);

    return dataDao.findUserByName(username);
  }

  public Optional<User> authenticateUser(String username, String password) {
    var maybeUser = dataDao.findUserByName(username);
    if (maybeUser.isEmpty()) {
      return Optional.empty();
    }

    var verificationResult = BCrypt
      .verifyer()
      .verify(password.toCharArray(), maybeUser.get().getPassword().toCharArray());
    if (verificationResult.verified) {
      return maybeUser;
    }
    return Optional.empty();
  }
}

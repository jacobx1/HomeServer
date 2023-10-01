package com.jacobx1.homeserver.service.dao;

import com.hubspot.rosetta.jdbi3.BindWithRosetta;
import com.hubspot.rosetta.jdbi3.RosettaRowMapperFactory;
import com.jacobx1.homeserver.service.model.User;
import com.jacobx1.homeserver.service.model.UserWithRole;
import java.util.Optional;
import org.jdbi.v3.sqlobject.config.RegisterRowMapperFactory;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.vyarus.guicey.jdbi3.installer.repository.JdbiRepository;
import ru.vyarus.guicey.jdbi3.tx.InTransaction;

@JdbiRepository
@InTransaction
@RegisterRowMapperFactory(RosettaRowMapperFactory.class)
public interface DataDao {
  @SqlQuery("select * from data where id=:id")
  User findNameForId(@Bind("id") int id);

  @SqlQuery("select * from data where name=:name LIMIT 1")
  Optional<User> findUserByName(@Bind("name") String name);

  @SqlUpdate("INSERT INTO data (name, password, role) VALUES (:name, :password, :role)")
  int insertUser(@BindWithRosetta UserWithRole user);
}

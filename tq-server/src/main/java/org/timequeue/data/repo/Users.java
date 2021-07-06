package org.timequeue.data.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.timequeue.data.model.User;

@Repository
public interface Users extends CrudRepository<User, String> {
}

package org.timequeue.data.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.timequeue.data.model.Event;
import org.timequeue.data.model.User;

import java.util.UUID;

@Repository
public interface Events extends CrudRepository<Event, UUID> {
}

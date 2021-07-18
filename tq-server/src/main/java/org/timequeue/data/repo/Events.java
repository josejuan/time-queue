package org.timequeue.data.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.timequeue.data.model.Event;
import org.timequeue.data.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface Events extends CrudRepository<Event, UUID> {
    @Query("select e from Event e where e.nextNotification < :now")
    List<Event> findPendingNotifications(@Param("now") LocalDateTime now);
}

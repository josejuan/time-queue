package org.timequeue.data.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tq_event")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class Event {

    @Id
    @Column(updatable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "userId", insertable = true, updatable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    /**
     * Is the date of the event.
     */
    @Column(nullable = false)
    private OffsetDateTime nextEvent;

    /**
     * Last notification sent for this event.
     */
    @Column
    private OffsetDateTime lastNotification;

    /**
     * (Ordered) minutes after event where notifications will be send.
     *
     *    -3600   -1800   -900   ...
     *      ^        ^      ^
     *     send!   send!   send!
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "tq_event_notification", joinColumns = @JoinColumn(name = "eventId"))
    @Column(name = "minutesAfter")
    private Set<Integer> minutesAfterNotifications = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UpdateMode updateMode;
}

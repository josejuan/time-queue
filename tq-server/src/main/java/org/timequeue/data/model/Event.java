package org.timequeue.data.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
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
    private String id = UUID.randomUUID().toString();

    @ManyToOne
    @JoinColumn(name = "userId", insertable = true, updatable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    /**
     * Is the date of the event (in the user local time zone)
     */
    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime nextEvent;

    /**
     * Next notification (utc)
     */
    @Column
    private LocalDateTime nextNotification;

    /**
     * Last notification sent for this event (utc)
     */
    @Column
    private LocalDateTime lastNotification;

    /**
     * (Ordered) minutes after event where notifications will be send.
     *
     *    -4660   -1730   -310   ...
     *      ^        ^      ^
     *     send!   send!   send!
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "tq_event_notification",
            joinColumns = @JoinColumn(name = "eventId"),
            foreignKey = @ForeignKey(
                    name = "eventId",
                    foreignKeyDefinition = "FOREIGN KEY (event_id) REFERENCES tq_event(id) ON DELETE CASCADE")
    )
    @Column(name = "minutesAfter")
    private Set<Integer> minutesAfterNotifications = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UpdateMode updateMode;
}

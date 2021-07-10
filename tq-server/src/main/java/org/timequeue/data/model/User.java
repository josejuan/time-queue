package org.timequeue.data.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Entity
@Table(name = "tq_user")
@Getter
@Setter
@EqualsAndHashCode(of = "email")
@NoArgsConstructor
public class User {

    @Id
    private String email;

    @Column(nullable = false)
    private String pwd;

    @Column(nullable = false)
    private boolean enabled = false;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<Event> events = new HashSet<>();
}

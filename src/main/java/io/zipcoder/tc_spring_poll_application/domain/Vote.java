package io.zipcoder.tc_spring_poll_application.domain;

import javax.persistence.*;

@Entity
public class Vote {
    @Id
    @GeneratedValue
    @Column(name = "VOTE_ID")
    Long id;

    @ManyToOne
    @JoinColumn(name = "OPTION_ID")
    Option option;

    public Long getId() {
        return id;
    }
}

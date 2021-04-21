package io.zipcoder.tc_spring_poll_application.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Option {
    @Id
    @GeneratedValue
    @Column
    Long id;
}

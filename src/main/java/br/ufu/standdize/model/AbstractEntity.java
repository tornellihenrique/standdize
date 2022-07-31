package br.ufu.standdize.model;

import lombok.experimental.SuperBuilder;

import java.util.Date;

@SuperBuilder(toBuilder = true)
public abstract class AbstractEntity {

    // Date
    protected Date date;
    protected Date lastUpdate;

    // Location
    protected String city;
    protected String region;
    protected String country;

}
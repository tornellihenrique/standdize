package br.ufu.standdize.model;

import br.ufu.standdize.model.dto.response.ServiceResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public abstract class AbstractEntity {

    // Date
    protected Date date;
    protected Date lastUpdate;

    // Location
    protected String city;
    protected String region;
    protected String country;

    public abstract ServiceResponse toResponse();

}
package br.ufu.standdize.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ServiceDetailsResponse {

    private String id;

    private String name;

    private String description;

    private Date lastUpdate;

}
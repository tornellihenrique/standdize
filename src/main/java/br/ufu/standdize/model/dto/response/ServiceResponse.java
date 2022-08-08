package br.ufu.standdize.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ServiceResponse {
    private String id;
    private String name;
    private String description;
    private Date date;
    private Date lastUpdate;
}

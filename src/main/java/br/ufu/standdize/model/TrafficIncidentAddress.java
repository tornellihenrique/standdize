package br.ufu.standdize.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "traffic_incident_address")
@Builder(toBuilder = true)
@Getter
public class TrafficIncidentAddress {
 
    @Id
    private String id;

    // Date
    private Date creationDate;
    private Date modificationDate;

    // Address
    private String address;

    @Builder.Default
    private Boolean active = Boolean.TRUE;

}
package br.ufu.standdize.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrafficIncidentItemAPIResponse {

    @JsonProperty("type")
    private String type;

    @JsonProperty("properties")
    private TrafficIncidentPropertiesAPIResponse properties;

    @JsonProperty("geometry")
    private TrafficIncidentGeometryAPIResponse geometry;

}
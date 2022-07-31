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
public class TrafficFlowSegmentAPIResponse {

    @JsonProperty("frc")
    private String frc;

    @JsonProperty("currentSpeed")
    private Double currentSpeed;

    @JsonProperty("freeFlowSpeed")
    private Double freeFlowSpeed;

    @JsonProperty("currentTravelTime")
    private Double currentTravelTime;

    @JsonProperty("freeFlowTravelTime")
    private Double freeFlowTravelTime;

    @JsonProperty("coordinates")
    private TrafficFlowSegmentCoordinatesAPIResponse coordinates;

}
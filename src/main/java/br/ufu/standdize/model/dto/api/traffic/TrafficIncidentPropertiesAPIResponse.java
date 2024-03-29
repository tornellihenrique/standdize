package br.ufu.standdize.model.dto.api.traffic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrafficIncidentPropertiesAPIResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("iconCategory")
    private Integer iconCategory;

    @JsonProperty("magnitudeOfDelay")
    private Integer magnitudeOfDelay;

}
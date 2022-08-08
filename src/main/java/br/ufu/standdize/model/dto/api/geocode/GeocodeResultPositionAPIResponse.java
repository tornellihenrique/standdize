package br.ufu.standdize.model.dto.api.geocode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeocodeResultPositionAPIResponse {

    @JsonProperty("lat")
    private Double lat;

    @JsonProperty("lon")
    private Double lon;

}
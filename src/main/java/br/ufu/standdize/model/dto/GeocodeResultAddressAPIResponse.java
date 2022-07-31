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
public class GeocodeResultAddressAPIResponse {

    @JsonProperty("municipality")
    private String municipality;

    @JsonProperty("countrySubdivision")
    private String countrySubdivision;

    @JsonProperty("countryCode")
    private String countryCode;

    @JsonProperty("country")
    private String country;

}
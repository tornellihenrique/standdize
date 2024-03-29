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
public class GeocodeResultAPIResponse {

    @JsonProperty("type")
    private GeocodeResultTypeAPIResponse type;

    @JsonProperty("address")
    private GeocodeResultAddressAPIResponse address;

    @JsonProperty("position")
    private GeocodeResultPositionAPIResponse position;

}
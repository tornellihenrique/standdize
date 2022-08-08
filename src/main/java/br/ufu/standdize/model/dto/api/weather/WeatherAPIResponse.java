package br.ufu.standdize.model.dto.api.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherAPIResponse {
    @JsonProperty("location")
    private WeatherLocationAPIResponse location;

    @JsonProperty("current")
    private WeatherCurrentAPIResponse current;
}
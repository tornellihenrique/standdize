package br.ufu.standdize.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.TimeZone;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherCurrentAPIResponse {
    // Date
    @JsonProperty("last_updated")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date lastUpdate;

    // Weather
    @JsonProperty("condition")
    private WeatherCurrentConditionAPIResponse condition;
    @JsonProperty("wind_kph")
    private Float windKph;
    @JsonProperty("wind_degree")
    private Float windDegree;
    @JsonProperty("wind_dir")
    private String windDir;
    @JsonProperty("pressure_mb")
    private Float pressureMb;
    @JsonProperty("pressure_in")
    private Float pressureIn;
    @JsonProperty("humidity")
    private Integer humidity;
    @JsonProperty("cloud")
    private Integer cloud;
}
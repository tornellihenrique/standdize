package br.ufu.standdize.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;

@Document(collection = "weather")
@Builder(toBuilder = true)
public class Weather {
 
    @Id
    private String id;

    // Date
    private OffsetDateTime date;
    private OffsetDateTime lastUpdate;

    // Weather
    private String condition;
    private String conditionIcon;
    private Integer conditionCode;
    private Float windKph;
    private Float windDegree;
    private String windDir;
    private Float pressureMb;
    private Float pressureIn;
    private Integer humidity;
    private Integer cloud;

    // Location
    private String city;
    private String region;
    private String country;

}
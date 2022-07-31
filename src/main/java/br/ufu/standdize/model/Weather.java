package br.ufu.standdize.model;

import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "weather")
@SuperBuilder(toBuilder = true)
public class Weather extends AbstractEntity {
 
    @Id
    private String id;

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

}
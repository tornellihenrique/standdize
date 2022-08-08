package br.ufu.standdize.model;

import br.ufu.standdize.model.dto.response.ServiceResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
class WeatherResponse extends ServiceResponse {
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

@NoArgsConstructor
@AllArgsConstructor
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

    @Override
    public ServiceResponse toResponse() {
        return WeatherResponse.builder()
                .id(id)
                .date(date)
                .condition(condition)
                .conditionIcon(conditionIcon)
                .conditionCode(conditionCode)
                .windKph(windKph)
                .windDegree(windDegree)
                .windDir(windDir)
                .pressureMb(pressureMb)
                .pressureIn(pressureIn)
                .humidity(humidity)
                .cloud(cloud)
                .build();
    }
}
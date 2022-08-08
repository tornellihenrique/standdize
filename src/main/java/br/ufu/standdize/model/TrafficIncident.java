package br.ufu.standdize.model;

import br.ufu.standdize.model.dto.api.traffic.TrafficFlowSegmentCoordinate;
import br.ufu.standdize.model.dto.response.ServiceResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
class TrafficIncidentResponse extends ServiceResponse {
    private String externalId;
    private String type;
    private Integer iconCategory;
    private Integer magnitudeOfDelay;
    private String geometryType;
    private List<List<Double>> coordinates;

    private String address;
}

@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "traffic_incident")
@SuperBuilder(toBuilder = true)
public class TrafficIncident extends AbstractEntity {
 
    @Id
    private String id;

    // Traffic
    private String externalId;
    private String type;
    private Integer iconCategory;
    private Integer magnitudeOfDelay;
    private String geometryType;
    private List<List<Double>> coordinates;

    // Address
    private String address;

    @Override
    public ServiceResponse toResponse() {
        return TrafficIncidentResponse.builder()
                .id(id)
                .date(date)
                .externalId(externalId)
                .type(type)
                .iconCategory(iconCategory)
                .magnitudeOfDelay(magnitudeOfDelay)
                .geometryType(geometryType)
                .coordinates(coordinates)
                .address(address)
                .build();
    }
}
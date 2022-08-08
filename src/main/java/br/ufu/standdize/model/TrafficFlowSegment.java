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
class TrafficFlowSegmentResponse extends ServiceResponse {
    private String frc;
    private Double currentSpeed;
    private Double freeFlowSpeed;
    private Double currentTravelTime;
    private Double freeFlowTravelTime;
    private List<TrafficFlowSegmentCoordinate> coordinates;

    private String address;
}

@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "traffic_flow_segment")
@SuperBuilder(toBuilder = true)
public class TrafficFlowSegment extends AbstractEntity {
 
    @Id
    private String id;

    // Traffic
    private String frc;
    private Double currentSpeed;
    private Double freeFlowSpeed;
    private Double currentTravelTime;
    private Double freeFlowTravelTime;
    private List<TrafficFlowSegmentCoordinate> coordinates;

    // Address
    private String address;

    @Override
    public ServiceResponse toResponse() {
        return TrafficFlowSegmentResponse.builder()
                .id(id)
                .date(date)
                .frc(frc)
                .currentSpeed(currentSpeed)
                .freeFlowSpeed(freeFlowSpeed)
                .currentTravelTime(currentTravelTime)
                .freeFlowTravelTime(freeFlowTravelTime)
                .coordinates(coordinates)
                .address(address)
                .build();
    }
}
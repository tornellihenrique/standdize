package br.ufu.standdize.model;

import br.ufu.standdize.model.dto.TrafficFlowSegmentCoordinate;
import br.ufu.standdize.model.dto.TrafficFlowSegmentCoordinatesAPIResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

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

}
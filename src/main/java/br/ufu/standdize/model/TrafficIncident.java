package br.ufu.standdize.model;

import br.ufu.standdize.model.dto.TrafficFlowSegmentCoordinate;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

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

}
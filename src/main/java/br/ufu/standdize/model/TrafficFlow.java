package br.ufu.standdize.model;

import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "traffic_flow")
@SuperBuilder(toBuilder = true)
public class TrafficFlow extends AbstractEntity {
 
    @Id
    private String id;

    // Traffic
    private List<String> tiles;

}
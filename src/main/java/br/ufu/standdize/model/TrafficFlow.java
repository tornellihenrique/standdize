package br.ufu.standdize.model;

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
class TrafficFlowResponse extends ServiceResponse {
    private List<String> tiles;
}

@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "traffic_flow")
@SuperBuilder(toBuilder = true)
public class TrafficFlow extends AbstractEntity {
 
    @Id
    private String id;

    // Traffic
    private List<String> tiles;

    @Override
    public ServiceResponse toResponse() {
        return TrafficFlowResponse.builder()
                .id(id)
                .date(date)
                .tiles(tiles)
                .build();
    }
}
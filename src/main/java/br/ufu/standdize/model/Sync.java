package br.ufu.standdize.model;

import br.ufu.standdize.model.dto.response.ServiceResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
class SyncResponse extends ServiceResponse {
    private String type;
    private Boolean hasError;
    private String errorMessage;
}

@Document(collection = "sync")
@Builder(toBuilder = true)
@Getter
public class Sync {
 
    @Id
    private String id;

    // Date
    private Date date;

    // Service
    private String type;

    // Error Handling
    private Boolean hasError;
    private String errorMessage;

    public ServiceResponse toResponse() {
        return SyncResponse.builder()
                .id(id)
                .date(date)
                .type(type)
                .hasError(hasError)
                .errorMessage(errorMessage)
                .build();
    }

}
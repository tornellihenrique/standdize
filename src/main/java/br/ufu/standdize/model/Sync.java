package br.ufu.standdize.model;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;

@Document(collection = "sync")
@Builder(toBuilder = true)
public class Sync {
 
    @Id
    private String id;

    // Date
    private OffsetDateTime date;

    // Service
    private String type;

    // Error Handling
    private Boolean hasError;
    private String errorMessage;

}
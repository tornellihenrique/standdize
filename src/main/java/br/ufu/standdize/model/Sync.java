package br.ufu.standdize.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;
import java.util.Date;

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

}
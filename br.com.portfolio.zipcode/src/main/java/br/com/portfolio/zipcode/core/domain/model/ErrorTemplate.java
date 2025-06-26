package br.com.portfolio.zipcode.core.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorTemplate {

    private Integer errorCode;
    private String message;
    private Timestamp timestamp;
    private String traceId;

}

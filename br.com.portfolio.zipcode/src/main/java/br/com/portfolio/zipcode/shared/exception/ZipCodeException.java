package br.com.portfolio.zipcode.shared.exception;

import br.com.portfolio.zipcode.shared.constant.GetTimestamp;
import feign.FeignException;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
public class ZipCodeException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private Integer errorCode;
    private String message;
    private Timestamp timestamp;
    private String traceId;

    public ZipCodeException(FeignException feignException) {
        this.errorCode = feignException.status();
        this.message = feignException.getMessage();
        this.timestamp = GetTimestamp.getTimestamp();
        this.traceId = setTraceId();
    }

    private String setTraceId() {
        return UUID.randomUUID().toString();
    }

}

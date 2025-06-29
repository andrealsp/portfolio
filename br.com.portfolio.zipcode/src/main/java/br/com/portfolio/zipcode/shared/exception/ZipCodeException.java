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

    public ZipCodeException(RuntimeException exception) {
        this.errorCode = determineErrorCode(exception);
        this.message = exception.getMessage();
        this.timestamp = GetTimestamp.getTimestamp();
        this.traceId = setTraceId();
    }

    public ZipCodeException(String message, Integer errorCode) {
        this.message = message;
        this.errorCode = errorCode;
        this.timestamp = GetTimestamp.getTimestamp();
        this.traceId = setTraceId();
    }

    private Integer determineErrorCode(RuntimeException exception) {
        if (exception instanceof FeignException feignException) {
            return feignException.status();
        } else if (exception instanceof IllegalArgumentException) {
            return 400; // Bad Request
        } else if (exception instanceof IllegalStateException) {
            return 409; // Conflict
        } else if (exception instanceof UnsupportedOperationException) {
            return 501; // Not Implemented
        } else if (exception instanceof SecurityException) {
            return 403; // Forbidden
        }
        return 500; // Internal Server Error para outras RuntimeExceptions
    }

    private String setTraceId() {
        return UUID.randomUUID().toString();
    }

}

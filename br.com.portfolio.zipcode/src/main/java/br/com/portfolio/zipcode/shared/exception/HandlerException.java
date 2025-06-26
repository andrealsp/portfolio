package br.com.portfolio.zipcode.shared.exception;

import br.com.portfolio.zipcode.core.domain.model.ErrorTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestControllerAdvice
public class HandlerException {

    private static final long serialVersionUID = 1L;

    @ExceptionHandler(ZipCodeException.class)
    public static ResponseEntity<ErrorTemplate> handleAddressRegistrationException(ZipCodeException e) {
        log.error("AddressRegistrationException: {}", e.getMessage());

        Integer code = e.getErrorCode();

        HttpStatus status;
        switch (code) {
            case 400:
                status = HttpStatus.BAD_REQUEST;
                break;
            case 401:
                status = HttpStatus.UNAUTHORIZED;
                break;
            case 403:
                status = HttpStatus.FORBIDDEN;
                break;
            case 404:
                status = HttpStatus.NOT_FOUND;
                break;
            case 409:
                status = HttpStatus.CONFLICT;
                break;
            case 415:
                status = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
                break;
            case 422:
                status = HttpStatus.UNPROCESSABLE_ENTITY;
                break;
            case 503:
                status = HttpStatus.SERVICE_UNAVAILABLE;
                break;
            default:
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                break;
        }

        ErrorTemplate errorBody = ErrorTemplate.builder()
                .errorCode(e.getErrorCode())
                .message(prepareMessage(e.getMessage()))
                .timestamp(e.getTimestamp())
                .traceId(e.getTraceId())
                .build();

        log.info("Returning error body: {}", errorBody.toString());
        return ResponseEntity.status(status).body(errorBody);
    }

    private static String prepareMessage(String message) {
        Pattern pattern = Pattern.compile("\"error\":\\s*\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            String errorMessage = matcher.group(1);
            return errorMessage;
        } else {
            return "Mensagem de erro não encontrada.";
        }
    }
}

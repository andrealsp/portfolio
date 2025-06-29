package br.com.portfolio.zipcode.api.rest;

import br.com.portfolio.zipcode.core.application.service.ZipcodeService;
import br.com.portfolio.zipcode.core.domain.model.ZipcodeRequest;
import br.com.portfolio.zipcode.core.domain.model.ZipcodeResponse;
import br.com.portfolio.zipcode.shared.exception.ZipCodeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class ZipCodeController implements ZipCodeAPI {

    private final ZipcodeService service;

    public ZipCodeController(ZipcodeService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<List<ZipcodeResponse>> searchAddress(ZipcodeRequest request) throws ZipCodeException {
        log.info("Class: ZipCodeController - method: searchByZipCode - zipcode: {}", request);
        return ResponseEntity.ok().body(service.searchAddress(request));
    }


}

package br.com.portfolio.zipcode.api.rest;

import br.com.portfolio.zipcode.core.application.service.ZipcodeService;
import br.com.portfolio.zipcode.core.domain.model.StreetResponse;
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
    public ResponseEntity<StreetResponse> searchByZipCode(String zipcode) throws ZipCodeException {
        log.info("Class: ZipCodeController - method: searchByZipCode - zipcode: {}", zipcode);
        return ResponseEntity.ok().body(service.searchByZipCode(zipcode));
    }

    @Override
    public ResponseEntity<List<StreetResponse>> searchByStreetName(String state, String city, String streetName) throws ZipCodeException {
        log.info("Class: ZipCodeController - method: searchByStreetName - state: {} - city: {}, streetName: {}", state, city, streetName);
        return ResponseEntity.ok().body(service.searchByStreetName(state, city, streetName));
    }

}

package br.com.portfolio.zipcode.api.rest;

import br.com.portfolio.zipcode.core.domain.model.ZipcodeRequest;
import br.com.portfolio.zipcode.core.domain.model.ZipcodeResponse;
import br.com.portfolio.zipcode.shared.exception.ZipCodeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping(value = "/search")
@Tag(name = "Postal Code", description = "API for searching addresses by postal code or street name")
public interface ZipCodeAPI {

    @GetMapping("/zipcode")
    @Operation(summary = "Search Address by Zipcode", description = "Returns the address details for a given zipcode")
    ResponseEntity<List<ZipcodeResponse>> searchAddress(@RequestBody ZipcodeRequest request) throws ZipCodeException;

}

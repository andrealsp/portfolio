package br.com.portfolio.zipcode.api.rest;

import br.com.portfolio.zipcode.core.domain.model.StreetResponse;
import br.com.portfolio.zipcode.shared.exception.ZipCodeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping(value = "/search")
@Tag(name = "Postal Code", description = "API for searching addresses by postal code or street name")
public interface ZipCodeAPI {

    @GetMapping("/{zipcode}")
    @Operation(summary = "Search Address by Zipcode", description = "Returns the address details for a given zipcode")
    ResponseEntity<StreetResponse> searchByZipCode(@PathVariable String zipcode) throws ZipCodeException;

    @GetMapping("/{state}/{city}/{streetName}")
    @Operation(summary = "Search Address by Street Name", description = "Returns list of addresses for a given street name")
    ResponseEntity<List<StreetResponse>> searchByStreetName(
            @PathVariable String state,
            @PathVariable String city,
            @PathVariable String streetName) throws ZipCodeException;
}

package br.com.portfolio.zipcode.infrastructure.rest.postalcode.client;

import br.com.portfolio.zipcode.core.domain.model.StreetResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
    name = "postalCodeClient",
    url = "${webservice.rest.postalcode.uri}"
)
public interface PostalCodeFeignClient {

    @GetMapping(value = "/{zipcode}/json")
    StreetResponse searchZipcode(@PathVariable("zipcode") String zipcode);

    @GetMapping(value = "/{state}/{city}/{streetName}/json")
    List<StreetResponse> searchByStreetName(
            @PathVariable("state") String state,
            @PathVariable("city") String city,
            @PathVariable("streetName") String streetName);
}

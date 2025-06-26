package br.com.portfolio.zipcode.infrastructure.rest.postalcode;

import br.com.portfolio.zipcode.core.domain.model.StreetResponse;
import br.com.portfolio.zipcode.infrastructure.rest.postalcode.client.PostalCodeFeignClient;
import br.com.portfolio.zipcode.shared.exception.ZipCodeException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class PostalCodeRest implements PostalCodePortOut {

    private final PostalCodeFeignClient client;

    public PostalCodeRest(PostalCodeFeignClient client) {
        this.client = client;
    }

    @Override
    public StreetResponse searchByZipcode(String zipcode) throws ZipCodeException {
        try {
            return client.searchZipcode(zipcode);
        } catch (FeignException ex) {
            log.error("Error searching zipcode: {}", zipcode, ex);
            throw new ZipCodeException(ex);
        }
    }

    @Override
    public List<StreetResponse> searchByStreetName(String state, String city, String streetName) throws ZipCodeException {
        try {
            return client.searchByStreetName(state, city, streetName);
        } catch (FeignException ex) {
            log.error("Error searching street: {} in city: {} - state: {}", streetName, city, state, ex);
            throw new ZipCodeException(ex);
        }
    }
}

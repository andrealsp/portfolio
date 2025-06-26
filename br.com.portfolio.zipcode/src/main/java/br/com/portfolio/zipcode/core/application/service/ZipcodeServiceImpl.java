package br.com.portfolio.zipcode.core.application.service;

import br.com.portfolio.zipcode.core.domain.model.StreetResponse;
import br.com.portfolio.zipcode.infrastructure.rest.postalcode.PostalCodePortOut;
import br.com.portfolio.zipcode.shared.exception.ZipCodeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ZipcodeServiceImpl implements ZipcodeService {

    private final PostalCodePortOut postalCode;

    public ZipcodeServiceImpl(PostalCodePortOut postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public StreetResponse searchByZipCode(String zipcode) throws ZipCodeException {
        log.info("Searching zipcode: {}", zipcode);
        return postalCode.searchByZipcode(zipcode);
    }

    @Override
    public List<StreetResponse> searchByStreetName(String state, String city, String streetName) throws ZipCodeException {
        log.info("Searching street: {} in city: {} - state: {}", streetName, city, state);
        return postalCode.searchByStreetName(state, city, streetName);
    }
}

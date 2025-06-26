package br.com.portfolio.zipcode.infrastructure.rest.postalcode;

import br.com.portfolio.zipcode.core.domain.model.StreetResponse;
import br.com.portfolio.zipcode.shared.exception.ZipCodeException;

import java.util.List;

public interface PostalCodePortOut {

    StreetResponse searchByZipcode(String zipcode) throws ZipCodeException;

    List<StreetResponse> searchByStreetName(String state, String city, String streetName) throws ZipCodeException;

}

package br.com.portfolio.zipcode.core.application.service;

import br.com.portfolio.zipcode.core.domain.model.StreetResponse;
import br.com.portfolio.zipcode.shared.exception.ZipCodeException;

import java.util.List;

public interface ZipcodeService {

    StreetResponse searchByZipCode(String zipcode) throws ZipCodeException;

    List<StreetResponse> searchByStreetName(String state, String city, String streetName) throws ZipCodeException;

}

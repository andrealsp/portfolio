package br.com.portfolio.zipcode.core.application.service;

import br.com.portfolio.zipcode.core.domain.model.ZipcodeRequest;
import br.com.portfolio.zipcode.core.domain.model.ZipcodeResponse;
import br.com.portfolio.zipcode.shared.exception.ZipCodeException;

import java.util.List;

public interface ZipcodeService {

    List<ZipcodeResponse> searchAddress(ZipcodeRequest request) throws ZipCodeException;

}

package br.com.portfolio.zipcode.core.application.service;

import br.com.portfolio.zipcode.core.application.service.mapper.ZipcodeResponseMapper;
import br.com.portfolio.zipcode.core.domain.model.ZipcodeRequest;
import br.com.portfolio.zipcode.core.domain.model.ZipcodeResponse;
import br.com.portfolio.zipcode.infrastructure.rest.postalcode.PostalCodePortOut;
import br.com.portfolio.zipcode.shared.exception.ZipCodeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ZipcodeServiceImpl implements ZipcodeService {

    private final PostalCodePortOut postalCode;

    private final ZipcodeResponseMapper prepareStreetResponse;

    public ZipcodeServiceImpl(PostalCodePortOut postalCode) {
        this.postalCode = postalCode;
        this.prepareStreetResponse = new ZipcodeResponseMapper();
    }

    @Override
    public List<ZipcodeResponse> searchAddress(ZipcodeRequest request) throws ZipCodeException {
        if (request == null) {
            log.error("Invalid ZipcodeRequest: request is null");
            throw new ZipCodeException("Invalid ZipcodeRequest: request is null", 400);
        }

        if (request.getZipcode() == null && request.getState() == null) {
            log.error("Invalid ZipcodeRequest: when zipcode is not provided, state is required. Request: {}", request);
            throw new ZipCodeException("Invalid ZipcodeRequest: when zipcode is not provided, state is required", 400);
        }

        if (request.getZipcode() != null) {
            return prepareStreetResponse.mapResponse(postalCode.searchByZipcode(request.getZipcode()));
        } else {
            return prepareStreetResponse.mapResponse(postalCode.searchByStreetName(request));
        }
    }

}

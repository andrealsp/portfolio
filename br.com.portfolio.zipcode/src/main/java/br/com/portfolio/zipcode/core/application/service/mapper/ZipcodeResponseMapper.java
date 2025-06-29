package br.com.portfolio.zipcode.core.application.service.mapper;

import br.com.portfolio.zipcode.core.domain.model.StreetResponse;
import br.com.portfolio.zipcode.core.domain.model.ZipcodeResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ZipcodeResponseMapper {

    public List<ZipcodeResponse> mapResponse(StreetResponse streetResponse) {
        if (streetResponse == null) {
            return List.of();
        }
        return List.of(convertToZipcodeResponse(streetResponse));
    }

    public List<ZipcodeResponse> mapResponse(List<StreetResponse> streetResponses) {
        if (streetResponses == null || streetResponses.isEmpty()) {
            return List.of();
        }

        return streetResponses.stream()
                .filter(Objects::nonNull)
                .map(response -> ZipcodeResponse.builder()
                        .zipcode(response.getZipcode())
                        .street(response.getStreet())
                        .complement(response.getComplement())
                        .neighborhood(response.getNeighborhood())
                        .city(response.getCity())
                        .state(response.getState())
                        .build())
                .collect(Collectors.toList());
    }

    private ZipcodeResponse convertToZipcodeResponse(StreetResponse response) {
        return ZipcodeResponse.builder()
                .zipcode(response.getZipcode())
                .street(response.getStreet())
                .complement(response.getComplement())
                .neighborhood(response.getNeighborhood())
                .city(response.getCity())
                .state(response.getState())
                .build();
    }

}

package br.com.portfolio.zipcode.api.rest;

import br.com.portfolio.zipcode.core.application.service.ZipcodeService;
import br.com.portfolio.zipcode.core.domain.model.ZipcodeRequest;
import br.com.portfolio.zipcode.core.domain.model.ZipcodeResponse;
import br.com.portfolio.zipcode.shared.exception.ZipCodeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ZipCodeControllerTest {

    @Mock
    private ZipcodeService zipcodeService;

    @InjectMocks
    private ZipCodeController controller;

    private ZipcodeRequest request;
    private ZipcodeResponse response;

    @BeforeEach
    void setUp() {
        response = ZipcodeResponse.builder()
                .zipcode("01001000")
                .street("Praça da Sé")
                .complement("lado ímpar")
                .neighborhood("Sé")
                .city("São Paulo")
                .state("SP")
                .stateFullName("São Paulo")
                .region("Sudeste")
                .areaCode("11")
                .build();
    }

    @Nested
    @DisplayName("Search Address Tests")
    class SearchAddressTests {

        @Test
        @DisplayName("Should return address when searching with valid ZIP code")
        void shouldReturnAddressWhenSearchingWithValidZipCode() throws ZipCodeException {
            // Arrange
            request = ZipcodeRequest.builder()
                    .zipcode("01001000")
                    .build();

            when(zipcodeService.searchAddress(any(ZipcodeRequest.class)))
                    .thenReturn(List.of(response));

            // Act
            ResponseEntity<List<ZipcodeResponse>> result = controller.searchAddress(request);

            // Assert
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(result.getBody())
                    .isNotNull()
                    .hasSize(1)
                    .first()
                    .satisfies(address -> {
                        assertThat(address.getZipcode()).isEqualTo("01001000");
                        assertThat(address.getStreet()).isEqualTo("Praça da Sé");
                        assertThat(address.getCity()).isEqualTo("São Paulo");
                        assertThat(address.getState()).isEqualTo("SP");
                    });
        }

        @Test
        @DisplayName("Should return address when searching with valid state and street")
        void shouldReturnAddressWhenSearchingWithValidStateAndStreet() throws ZipCodeException {
            // Arrange
            request = ZipcodeRequest.builder()
                    .streetName("Praça da Sé")
                    .state("SP")
                    .build();

            when(zipcodeService.searchAddress(any(ZipcodeRequest.class)))
                    .thenReturn(List.of(response));

            // Act
            ResponseEntity<List<ZipcodeResponse>> result = controller.searchAddress(request);

            // Assert
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(result.getBody()).isNotNull().hasSize(1);
        }

        @Test
        @DisplayName("Should handle ZipCodeException when service throws error")
        void shouldHandleZipCodeExceptionWhenServiceThrowsError() {
            // Arrange
            request = ZipcodeRequest.builder().build();

            when(zipcodeService.searchAddress(any(ZipcodeRequest.class)))
                    .thenThrow(new ZipCodeException("Invalid request", 400));

            // Act & Assert
            assertThatThrownBy(() -> controller.searchAddress(request))
                    .isInstanceOf(ZipCodeException.class)
                    .hasMessageContaining("Invalid request");
        }

        @Test
        @DisplayName("Should return empty list when no addresses found")
        void shouldReturnEmptyListWhenNoAddressesFound() throws ZipCodeException {
            // Arrange
            request = ZipcodeRequest.builder()
                    .streetName("Non existent street")
                    .state("SP")
                    .build();

            when(zipcodeService.searchAddress(any(ZipcodeRequest.class)))
                    .thenReturn(List.of());

            // Act
            ResponseEntity<List<ZipcodeResponse>> result = controller.searchAddress(request);

            // Assert
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(result.getBody())
                    .isNotNull()
                    .isEmpty();
        }
    }
}

package br.com.portfolio.zipcode.core.application.service;

import br.com.portfolio.zipcode.core.domain.model.StreetResponse;
import br.com.portfolio.zipcode.core.domain.model.ZipcodeRequest;
import br.com.portfolio.zipcode.core.domain.model.ZipcodeResponse;
import br.com.portfolio.zipcode.infrastructure.rest.postalcode.PostalCodePortOut;
import br.com.portfolio.zipcode.shared.exception.ZipCodeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ZipcodeServiceImplTest {

    @Mock
    private PostalCodePortOut postalCode;

    private ZipcodeServiceImpl service;
    private StreetResponse streetResponse;

    @BeforeEach
    void setUp() {
        service = new ZipcodeServiceImpl(postalCode);

        streetResponse = new StreetResponse();
        streetResponse.setZipcode("01001000");
        streetResponse.setStreet("Praça da Sé");
        streetResponse.setComplement("lado ímpar");
        streetResponse.setNeighborhood("Sé");
        streetResponse.setCity("São Paulo");
        streetResponse.setState("SP");
    }

    @Nested
    @DisplayName("Input Validation Tests")
    class InputValidationTests {

        @Test
        @DisplayName("Should throw ZipCodeException when request is null")
        void shouldThrowZipCodeExceptionWhenRequestIsNull() {
            assertThatExceptionOfType(ZipCodeException.class)
                .isThrownBy(() -> service.searchAddress(null))
                .withMessage("Invalid ZipcodeRequest: request is null");
        }

        @Test
        @DisplayName("Should throw ZipCodeException when both zipcode and state are null")
        void shouldThrowZipCodeExceptionWhenBothZipcodeAndStateAreNull() {
            ZipcodeRequest request = ZipcodeRequest.builder().build();

            assertThatExceptionOfType(ZipCodeException.class)
                .isThrownBy(() -> service.searchAddress(request))
                .withMessage("Invalid ZipcodeRequest: when zipcode is not provided, state is required");
        }
    }

    @Nested
    @DisplayName("Search By Zipcode Tests")
    class SearchByZipcodeTests {

        @Test
        @DisplayName("Should return address when searching with valid zipcode")
        void shouldReturnAddressWhenSearchingWithValidZipcode() throws ZipCodeException {
            // Arrange
            ZipcodeRequest request = ZipcodeRequest.builder()
                    .zipcode("01001000")
                    .build();

            when(postalCode.searchByZipcode(anyString()))
                    .thenReturn(streetResponse);

            // Act
            List<ZipcodeResponse> result = service.searchAddress(request);

            // Assert
            verify(postalCode).searchByZipcode("01001000");
            assertThat(result)
                    .isNotNull()
                    .hasSize(1)
                    .first()
                    .satisfies(response -> {
                        assertThat(response.getZipcode()).isEqualTo("01001000");
                        assertThat(response.getStreet()).isEqualTo("Praça da Sé");
                        assertThat(response.getCity()).isEqualTo("São Paulo");
                        assertThat(response.getState()).isEqualTo("SP");
                    });
        }

        @Test
        @DisplayName("Should handle ZipCodeException from postal service")
        void shouldHandleZipCodeExceptionFromPostalService() {
            // Arrange
            ZipcodeRequest request = ZipcodeRequest.builder()
                    .zipcode("00000000")
                    .build();

            when(postalCode.searchByZipcode(anyString()))
                    .thenThrow(new ZipCodeException("CEP not found", 404));

            // Act & Assert
            assertThatExceptionOfType(ZipCodeException.class)
                .isThrownBy(() -> service.searchAddress(request))
                .satisfies(ex -> {
                    assertThat(ex.getMessage()).isEqualTo("CEP not found");
                    assertThat(ex.getErrorCode()).isEqualTo(404);
                });
        }
    }

    @Nested
    @DisplayName("Search By Street Tests")
    class SearchByStreetTests {

        @Test
        @DisplayName("Should return addresses when searching with valid street and state")
        void shouldReturnAddressesWhenSearchingWithValidStreetAndState() throws ZipCodeException {
            // Arrange
            ZipcodeRequest request = ZipcodeRequest.builder()
                    .streetName("Praça da Sé")
                    .city("São Paulo")
                    .state("SP")
                    .build();

            when(postalCode.searchByStreetName(any(ZipcodeRequest.class)))
                    .thenReturn(List.of(streetResponse));

            // Act
            List<ZipcodeResponse> result = service.searchAddress(request);

            // Assert
            verify(postalCode).searchByStreetName(request);
            assertThat(result)
                    .isNotNull()
                    .hasSize(1)
                    .first()
                    .satisfies(response -> {
                        assertThat(response.getStreet()).isEqualTo("Praça da Sé");
                        assertThat(response.getCity()).isEqualTo("São Paulo");
                        assertThat(response.getState()).isEqualTo("SP");
                    });
        }

        @Test
        @DisplayName("Should handle ZipCodeException from postal service in street search")
        void shouldHandleZipCodeExceptionFromPostalServiceInStreetSearch() {
            // Arrange
            ZipcodeRequest request = ZipcodeRequest.builder()
                    .streetName("Invalid Street")
                    .state("SP")
                    .build();

            when(postalCode.searchByStreetName(any(ZipcodeRequest.class)))
                    .thenThrow(new ZipCodeException("Street not found", 404));

            // Act & Assert
            assertThatExceptionOfType(ZipCodeException.class)
                .isThrownBy(() -> service.searchAddress(request))
                .satisfies(ex -> {
                    assertThat(ex.getMessage()).isEqualTo("Street not found");
                    assertThat(ex.getErrorCode()).isEqualTo(404);
                });
        }
    }
}

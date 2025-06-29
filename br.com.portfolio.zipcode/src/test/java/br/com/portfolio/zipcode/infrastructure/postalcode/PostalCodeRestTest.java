package br.com.portfolio.zipcode.infrastructure.postalcode;

import br.com.portfolio.zipcode.core.domain.model.StreetResponse;
import br.com.portfolio.zipcode.core.domain.model.ZipcodeRequest;
import br.com.portfolio.zipcode.infrastructure.rest.postalcode.PostalCodeRest;
import br.com.portfolio.zipcode.infrastructure.rest.postalcode.client.PostalCodeFeignClient;
import br.com.portfolio.zipcode.shared.exception.ZipCodeException;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostalCodeRestTest {

    @Mock
    private PostalCodeFeignClient client;

    private PostalCodeRest postalCodeRest;
    private StreetResponse streetResponse;
    private Request mockRequest;

    @BeforeEach
    void setUp() {
        postalCodeRest = new PostalCodeRest(client);
        Map<String, Collection<String>> headers = new HashMap<>();
        headers.put("Content-Type", Collections.singletonList("application/json"));

        mockRequest = Request.create(
            Request.HttpMethod.GET,
            "/api/v1/zipcode",
            headers,
            Request.Body.empty(),
            new RequestTemplate()
        );

        streetResponse = new StreetResponse();
        streetResponse.setZipcode("01001000");
        streetResponse.setStreet("Praça da Sé");
        streetResponse.setComplement("lado ímpar");
        streetResponse.setNeighborhood("Sé");
        streetResponse.setCity("São Paulo");
        streetResponse.setState("SP");
    }

    @Nested
    @DisplayName("Search By Zipcode Tests")
    class SearchByZipcodeTests {

        @Test
        @DisplayName("Should successfully find address by zipcode")
        void shouldSuccessfullyFindAddressByZipcode() throws ZipCodeException {
            // Arrange
            String zipcode = "01001000";
            when(client.searchZipcode(eq(zipcode)))
                    .thenReturn(streetResponse);

            // Act
            StreetResponse result = postalCodeRest.searchByZipcode(zipcode);

            // Assert
            verify(client).searchZipcode(zipcode);
            assertThat(result)
                    .isNotNull()
                    .satisfies(response -> {
                        assertThat(response.getZipcode()).isEqualTo(zipcode);
                        assertThat(response.getStreet()).isEqualTo("Praça da Sé");
                        assertThat(response.getComplement()).isEqualTo("lado ímpar");
                        assertThat(response.getNeighborhood()).isEqualTo("Sé");
                        assertThat(response.getCity()).isEqualTo("São Paulo");
                        assertThat(response.getState()).isEqualTo("SP");
                    });
        }

        @Test
        @DisplayName("Should throw ZipCodeException when zipcode not found")
        void shouldThrowZipCodeExceptionWhenZipcodeNotFound() {
            // Arrange
            String zipcode = "00000000";
            byte[] errorBody = "{\"timestamp\":\"2024-01-01T10:00:00\",\"status\":404,\"error\":\"Not Found\",\"message\":\"CEP não encontrado\"}".getBytes(StandardCharsets.UTF_8);
            Map<String, Collection<String>> headers = new HashMap<>();
            headers.put("Content-Type", Collections.singletonList("application/json"));

            FeignException notFoundException = new FeignException.NotFound(
                "Not Found",
                mockRequest,
                errorBody,
                headers
            );

            when(client.searchZipcode(eq(zipcode)))
                    .thenThrow(notFoundException);

            // Act & Assert
            assertThatThrownBy(() -> postalCodeRest.searchByZipcode(zipcode))
                    .isInstanceOf(ZipCodeException.class)
                    .hasMessage(notFoundException.getMessage());
        }

        @Test
        @DisplayName("Should throw ZipCodeException when server error occurs")
        void shouldThrowZipCodeExceptionWhenServerErrorOccurs() {
            // Arrange
            String zipcode = "01001000";
            byte[] errorBody = "{\"timestamp\":\"2024-01-01T10:00:00\",\"status\":500,\"error\":\"Internal Server Error\",\"message\":\"Unexpected error occurred\"}".getBytes(StandardCharsets.UTF_8);
            Map<String, Collection<String>> headers = new HashMap<>();
            headers.put("Content-Type", Collections.singletonList("application/json"));

            FeignException serverError = new FeignException.InternalServerError(
                "Internal Server Error",
                mockRequest,
                errorBody,
                headers
            );

            when(client.searchZipcode(eq(zipcode)))
                    .thenThrow(serverError);

            // Act & Assert
            assertThatThrownBy(() -> postalCodeRest.searchByZipcode(zipcode))
                    .isInstanceOf(ZipCodeException.class)
                    .hasMessage(serverError.getMessage());
        }
    }

    @Nested
    @DisplayName("Search By Street Name Tests")
    class SearchByStreetNameTests {

        @Test
        @DisplayName("Should successfully find addresses by street name")
        void shouldSuccessfullyFindAddressesByStreetName() throws ZipCodeException {
            // Arrange
            ZipcodeRequest request = ZipcodeRequest.builder()
                    .streetName("Praça da Sé")
                    .city("São Paulo")
                    .state("SP")
                    .build();

            when(client.searchByStreetName(eq("SP"), eq("São Paulo"), eq("Praça da Sé")))
                    .thenReturn(List.of(streetResponse));

            // Act
            List<StreetResponse> result = postalCodeRest.searchByStreetName(request);

            // Assert
            verify(client).searchByStreetName("SP", "São Paulo", "Praça da Sé");
            assertThat(result)
                    .isNotNull()
                    .hasSize(1)
                    .first()
                    .satisfies(response -> {
                        assertThat(response.getStreet()).isEqualTo("Praça da Sé");
                        assertThat(response.getCity()).isEqualTo("São Paulo");
                        assertThat(response.getState()).isEqualTo("SP");
                        assertThat(response.getNeighborhood()).isEqualTo("Sé");
                    });
        }

        @Test
        @DisplayName("Should return empty list when no addresses found")
        void shouldReturnEmptyListWhenNoAddressesFound() throws ZipCodeException {
            // Arrange
            ZipcodeRequest request = ZipcodeRequest.builder()
                    .streetName("Rua Inexistente")
                    .state("SP")
                    .build();

            when(client.searchByStreetName(eq("SP"), eq(null), eq("Rua Inexistente")))
                    .thenReturn(Collections.emptyList());

            // Act
            List<StreetResponse> result = postalCodeRest.searchByStreetName(request);

            // Assert
            verify(client).searchByStreetName("SP", null, "Rua Inexistente");
            assertThat(result)
                    .isNotNull()
                    .isEmpty();
        }

        @Test
        @DisplayName("Should throw ZipCodeException when search fails with server error")
        void shouldThrowZipCodeExceptionWhenSearchFailsWithServerError() {
            // Arrange
            ZipcodeRequest request = ZipcodeRequest.builder()
                    .streetName("Praça da Sé")
                    .state("SP")
                    .build();

            byte[] errorBody = "{\"timestamp\":\"2024-01-01T10:00:00\",\"status\":500,\"error\":\"Internal Server Error\",\"message\":\"Error processing street search\"}".getBytes(StandardCharsets.UTF_8);
            Map<String, Collection<String>> headers = new HashMap<>();
            headers.put("Content-Type", Collections.singletonList("application/json"));

            FeignException serverError = new FeignException.InternalServerError(
                "Internal Server Error",
                mockRequest,
                errorBody,
                headers
            );

            when(client.searchByStreetName(anyString(), anyString(), anyString()))
                    .thenThrow(serverError);

            // Act & Assert
            assertThatThrownBy(() -> postalCodeRest.searchByStreetName(request))
                    .isInstanceOf(ZipCodeException.class)
                    .hasMessage(serverError.getMessage());
        }
    }
}

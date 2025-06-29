package br.com.portfolio.zipcode.core.application.service.mapper;

import br.com.portfolio.zipcode.core.domain.model.StreetResponse;
import br.com.portfolio.zipcode.core.domain.model.ZipcodeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ZipcodeResponseMapperTest {

    private ZipcodeResponseMapper mapper;
    private StreetResponse streetResponse;

    @BeforeEach
    void setUp() {
        mapper = new ZipcodeResponseMapper();
        streetResponse = new StreetResponse();
        streetResponse.setZipcode("01001000");
        streetResponse.setStreet("Praça da Sé");
        streetResponse.setComplement("lado ímpar");
        streetResponse.setNeighborhood("Sé");
        streetResponse.setCity("São Paulo");
        streetResponse.setState("SP");
    }

    @Nested
    @DisplayName("Single Response Mapping Tests")
    class SingleResponseMappingTests {

        @Test
        @DisplayName("Should correctly map single StreetResponse to ZipcodeResponse")
        void shouldCorrectlyMapSingleStreetResponseToZipcodeResponse() {
            // Act
            List<ZipcodeResponse> result = mapper.mapResponse(streetResponse);

            // Assert
            assertThat(result)
                .isNotNull()
                .hasSize(1)
                .first()
                .satisfies(response -> {
                    assertThat(response.getZipcode()).isEqualTo("01001000");
                    assertThat(response.getStreet()).isEqualTo("Praça da Sé");
                    assertThat(response.getComplement()).isEqualTo("lado ímpar");
                    assertThat(response.getNeighborhood()).isEqualTo("Sé");
                    assertThat(response.getCity()).isEqualTo("São Paulo");
                    assertThat(response.getState()).isEqualTo("SP");
                });
        }

        @Test
        @DisplayName("Should return empty list when StreetResponse is null")
        void shouldReturnEmptyListWhenStreetResponseIsNull() {
            // Act
            List<ZipcodeResponse> result = mapper.mapResponse((StreetResponse) null);

            // Assert
            assertThat(result)
                .isNotNull()
                .isEmpty();
        }
    }

    @Nested
    @DisplayName("List Response Mapping Tests")
    class ListResponseMappingTests {

        @Test
        @DisplayName("Should correctly map multiple StreetResponses")
        void shouldCorrectlyMapMultipleStreetResponses() {
            // Arrange
            StreetResponse secondResponse = new StreetResponse();
            secondResponse.setZipcode("01002000");
            secondResponse.setStreet("Rua Direita");
            secondResponse.setCity("São Paulo");
            secondResponse.setState("SP");

            List<StreetResponse> responses = Arrays.asList(streetResponse, secondResponse);

            // Act
            List<ZipcodeResponse> result = mapper.mapResponse(responses);

            // Assert
            assertThat(result)
                .isNotNull()
                .hasSize(2)
                .satisfies(list -> {
                    assertThat(list.get(0).getZipcode()).isEqualTo("01001000");
                    assertThat(list.get(0).getStreet()).isEqualTo("Praça da Sé");
                    assertThat(list.get(1).getZipcode()).isEqualTo("01002000");
                    assertThat(list.get(1).getStreet()).isEqualTo("Rua Direita");
                });
        }

        @Test
        @DisplayName("Should return empty list when input list is null")
        void shouldReturnEmptyListWhenInputListIsNull() {
            // Act
            List<ZipcodeResponse> result = mapper.mapResponse((List<StreetResponse>) null);

            // Assert
            assertThat(result)
                .isNotNull()
                .isEmpty();
        }

        @Test
        @DisplayName("Should return empty list when input list is empty")
        void shouldReturnEmptyListWhenInputListIsEmpty() {
            // Act
            List<ZipcodeResponse> result = mapper.mapResponse(List.of());

            // Assert
            assertThat(result)
                .isNotNull()
                .isEmpty();
        }

        @Test
        @DisplayName("Should filter out null elements from input list")
        void shouldFilterOutNullElementsFromInputList() {
            // Arrange
            List<StreetResponse> responses = Arrays.asList(streetResponse, null);

            // Act
            List<ZipcodeResponse> result = mapper.mapResponse(responses);

            // Assert
            assertThat(result)
                .isNotNull()
                .hasSize(1)
                .first()
                .satisfies(response -> {
                    assertThat(response.getZipcode()).isEqualTo("01001000");
                    assertThat(response.getStreet()).isEqualTo("Praça da Sé");
                });
        }
    }
}

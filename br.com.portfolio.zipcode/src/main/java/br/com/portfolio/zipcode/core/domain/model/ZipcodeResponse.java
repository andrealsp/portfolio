package br.com.portfolio.zipcode.core.domain.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZipcodeResponse {

    @JsonAlias("cep")
    private String zipcode;

    @JsonAlias("logradouro")
    private String street;

    @JsonAlias("complemento")
    private String complement;

    @JsonAlias("unidade")
    private String unit;

    @JsonAlias("bairro")
    private String neighborhood;

    @JsonAlias("localidade")
    private String city;

    @JsonAlias("uf")
    private String state;

    @JsonAlias("estado")
    private String stateFullName;

    @JsonAlias("regiao")
    private String region;

    private String ibge;
    private String gia;

    @JsonAlias("ddd")
    private String areaCode;

    private String siafi;

}

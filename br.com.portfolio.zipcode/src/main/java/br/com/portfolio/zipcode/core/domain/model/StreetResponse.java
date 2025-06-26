package br.com.portfolio.zipcode.core.domain.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StreetResponse {

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

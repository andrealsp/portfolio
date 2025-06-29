package br.com.portfolio.zipcode.core.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ZipcodeRequest {

    private String zipcode;
    private String streetName;
    private String city;
    private String state;

    public ZipcodeRequest(String zipcode, String streetName, String city, String state) {
        this.zipcode = zipcode;
        this.streetName = streetName;
        this.city = city;
        this.state = state;
    }

}

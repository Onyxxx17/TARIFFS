package com.tariff.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // 404 Error
public class TariffRuleNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public TariffRuleNotFoundException(Long id) {
        super("Could not find tariff rule " + id);
    } 

    public TariffRuleNotFoundException(String fromCountry, String toCountry, int year, Long product_id){
        super("Could not find applicable tariff rate for product code " + product_id + " from " + fromCountry + " to " + toCountry + " in " + year);
    }
}

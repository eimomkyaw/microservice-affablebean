package com.example.paymentgateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InsuffientAmount extends ResponseStatusException {
    public InsuffientAmount(){
        super(HttpStatus.BAD_REQUEST,"Insuffient Amount");
    }
}

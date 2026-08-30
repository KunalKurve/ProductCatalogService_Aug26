package dev.kunal.productcatalogservice_aug26.controllers;

import dev.kunal.productcatalogservice_aug26.exceptions.ProductAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // This makes my exception handlers global
public class ControllerAdvisor {

    @ExceptionHandler({IllegalArgumentException.class})
    private ResponseEntity<String> handleIllegalArgumentException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    private ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductAlreadyExistsException.class)
    private ResponseEntity<String> handleProductAlreadyExistsException(ProductAlreadyExistsException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }
}



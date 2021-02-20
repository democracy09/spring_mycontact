package com.java.project2.mycontact.exception;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PersonNotFoundException extends RuntimeException{

    private static final String MESSAGE = "Person Entity is not found";

    public PersonNotFoundException(){
        super(MESSAGE);
        log.error(MESSAGE);
    }
}

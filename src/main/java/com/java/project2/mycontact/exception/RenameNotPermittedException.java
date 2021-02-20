package com.java.project2.mycontact.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RenameNotPermittedException extends RuntimeException {

    private static final String MESSAGE = "rename is not permitted";

    public RenameNotPermittedException(){
        super(MESSAGE);
        log.error(MESSAGE);
    }

}

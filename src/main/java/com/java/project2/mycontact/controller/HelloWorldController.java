package com.java.project2.mycontact.controller;


import com.java.project2.mycontact.exception.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloWorldController {

    @GetMapping("/api/helloWorld")
    public String helloWorld(){
        return "HelloWorld";
    }

    @GetMapping("/api/helloException")
    public String helloException(){
        throw new RuntimeException("Hello RuntimeException");
    }

}

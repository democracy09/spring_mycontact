package com.java.project2.mycontact.controller.dto;


import com.java.project2.mycontact.domain.Person;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;


@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Accessors(chain = true)
public class PersonDto {

    private String name;
    private String hobby;
    private String address;
    private LocalDate birthday;
    private String job;
    private String phoneNumber;


}

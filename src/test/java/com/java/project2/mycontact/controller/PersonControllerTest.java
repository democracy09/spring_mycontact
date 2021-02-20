package com.java.project2.mycontact.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.project2.mycontact.controller.dto.PersonDto;
import com.java.project2.mycontact.domain.Person;
import com.java.project2.mycontact.domain.dto.Birthday;
import com.java.project2.mycontact.exception.handler.GlobalExceptionHandler;
import com.java.project2.mycontact.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@Slf4j
@Transactional
class PersonControllerTest {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    void beforeEach(){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .alwaysDo(print())
                .build();
    }

    @Test
    void getPerson() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/person/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("aa"))
                .andExpect(jsonPath("hobby").isEmpty())
                .andExpect(jsonPath("address").isEmpty())
                .andExpect(jsonPath("$.birthday").value("1991-08-15"))
                .andExpect(jsonPath("$.job").isEmpty())
                .andExpect(jsonPath("$.phoneNumber").isEmpty())
                .andExpect(jsonPath("$.deleted").value(false))
                .andExpect(jsonPath("$.age").isNumber())
                .andExpect(jsonPath("$.birthdayToday").isBoolean());



//        jsonPath("$.name").value("aa"); -> json 검증
//        assertThat(result.getName()).isEqualTo("aa"); -> 객체검증
    }



    @Test
    void postPerson() throws Exception{

        PersonDto dto = PersonDto.of("ee", "programming", "gg", LocalDate.now(), "programmer", "010-555-5555");

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonString(dto)))
                .andExpect(status().isCreated());

        Person result = personRepository.findAll(Sort.by(Sort.Direction.DESC, "id")).get(0);

        assertAll(
                ()->assertThat(result.getName()).isEqualTo("ee"),
                ()->assertThat(result.getHobby()).isEqualTo("programming"),
                ()->assertThat(result.getAddress()).isEqualTo("gg"),
                ()->assertThat(result.getBirthday()).isEqualTo(Birthday.of(LocalDate.now())),
                ()->assertThat(result.getJob()).isEqualTo("programmer"),
                ()->assertThat(result.getPhoneNumber()).isEqualTo("010-555-5555")
        );
    }

    @Test
    void postPersonIfNameIsNull() throws Exception{
        PersonDto dto = new PersonDto();

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("이름은 필수값입니다"));
    }

    @Test
    void postPersonIfNameIsEmptyString() throws Exception{
        PersonDto dto = new PersonDto();
        dto.setName("");

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("이름은 필수값입니다"));
    }

    @Test
    void postPersonIfNameIsBlankString() throws Exception{
        PersonDto dto = new PersonDto();
        dto.setName(" ");

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("이름은 필수값입니다"));
    }

    @Test
    void modifyPerson() throws Exception{

        PersonDto dto = PersonDto.of("aa", "programming", "df", LocalDate.now(), "programmer", "010-1111-2222");

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/person/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonString(dto)))
                .andExpect(status().isOk());

        Person result = personRepository.findById(1L).get();

        assertAll(
                () -> assertThat(result.getName()).isEqualTo("aa"),
                () -> assertThat(result.getHobby()).isEqualTo("programming"),
                () -> assertThat(result.getAddress()).isEqualTo("df"),
                () -> assertThat(result.getBirthday()).isEqualTo(new Birthday(LocalDate.now())),
                () -> assertThat(result.getJob()).isEqualTo("programmer"),
                () -> assertThat(result.getPhoneNumber()).isEqualTo("010-1111-2222")
        );
    }

    @Test
    void modifyPersonIfNameIsDiff() throws Exception{

        PersonDto dto = PersonDto.of("fds", "programming", "df", LocalDate.now(), "programmer", "010-1111-2222");


        mockMvc.perform(
        MockMvcRequestBuilders.put("/api/person/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(dto)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(400))
        .andExpect(jsonPath("$.message").value("rename is not permitted"));
    }

    @Test
    void modifyPersonIfPersonNotFound() throws Exception{
        PersonDto dto = PersonDto.of("aa","programming","seoul", LocalDate.now(), "programmer","010-2222-2222");

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/person/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("Person Entity is not found"));
    }

    @Test
    void modifyName() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/person/1")
                    .param("name", "modified"))
                .andExpect(status().isOk());

        assertThat(personRepository.findById(1L).get().getName().equals("modified"));
    }

    @Test
    void deletePerson() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/person/1"))
                .andExpect(status().isOk());

        assertTrue(personRepository.findPeopleDeleted().stream().anyMatch(person -> person.getId().equals(1L)));
    }

    @Test
    void checkJsonString() throws JsonProcessingException{
        PersonDto dto = new PersonDto();
        dto.setName("abcd")
                .setBirthday(LocalDate.now())
                .setAddress("ㅁ");

        System.out.println(">>> "+toJsonString(dto));

    }

    private String toJsonString(PersonDto personDto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(personDto);
    }

}
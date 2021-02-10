package com.java.project2.mycontact.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.project2.mycontact.controller.dto.PersonDto;
import com.java.project2.mycontact.domain.Person;
import com.java.project2.mycontact.domain.dto.Birthday;
import com.java.project2.mycontact.repository.PersonRepository;
import jdk.vm.ci.meta.Local;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import org.springframework.web.util.NestedServletException;

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
    private PersonController personController;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;
    @Autowired
    private MappingJackson2HttpMessageConverter messageConverter;

    @BeforeEach
    void beforeEach(){
        mockMvc = MockMvcBuilders.standaloneSetup(personController).setMessageConverters(messageConverter).build();
    }

    @Test
    void getPerson() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/person/1"))
                .andDo(print())
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
                .andDo(print())
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
    void modifyPerson() throws Exception{

        PersonDto dto = PersonDto.of("aa", "programming", "df", LocalDate.now(), "programmer", "010-1111-2222");

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/person/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonString(dto)))
                .andDo(print())
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

        assertThrows(NestedServletException.class, () ->
                mockMvc.perform(
                MockMvcRequestBuilders.put("/api/person/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonString(dto)))
                .andDo(print())
                .andExpect(status().isOk()));

    }

    @Test
    void modifyName() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/person/1")
                    .param("name", "modified"))
                .andDo(print())
                .andExpect(status().isOk());

        assertThat(personRepository.findById(1L).get().getName().equals("modified"));
    }

    @Test
    void deletePerson() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/person/1"))
                .andDo(print())
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
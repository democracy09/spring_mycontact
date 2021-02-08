package com.java.project2.mycontact.service;

import com.java.project2.mycontact.domain.Block;
import com.java.project2.mycontact.domain.Person;
import com.java.project2.mycontact.repository.BlockRepository;
import com.java.project2.mycontact.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class PersonServiceTest {

    @Autowired
    private PersonService personService;

    @Test
    void getPeopleExcludesBlocks(){
        List<Person> result = personService.getPeopleExcludeBlocks();

        assertThat(result.size()).isEqualTo(3);
        assertThat(result.get(0).getName()).isEqualTo("aa");
    }

    @Test
    void getPeopleByName(){
        List<Person> result = personService.getPeopleByName("dd");

        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void getPerson(){
        Person person = personService.getPerson(3L);

        assertThat(person.getName()).isEqualTo("bb");
    }

}
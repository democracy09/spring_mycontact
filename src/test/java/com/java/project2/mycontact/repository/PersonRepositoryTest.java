package com.java.project2.mycontact.repository;

import com.java.project2.mycontact.domain.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Test
    void crud(){
        Person person = new Person();
        person.setName("woong");
        person.setAge(23);
        person.setBloodType("B");

        personRepository.save(person);

        List<Person> people = personRepository.findAll();

        System.out.println(personRepository.findAll());
        assertThat(people.size()).isEqualTo(1);
        assertThat(people.get(0).getName()).isEqualTo("woong");
        assertThat(people.get(0).getAge()).isEqualTo(23);
        assertThat(people.get(0).getBloodType()).isEqualTo("B");

    }
}
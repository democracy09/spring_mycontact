package com.java.project2.mycontact.repository;

import com.java.project2.mycontact.domain.Person;
import com.java.project2.mycontact.domain.dto.Birthday;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
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

        List<Person> result = personRepository.findByName("fuck");

        System.out.println(personRepository.findAll());
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getName()).isEqualTo("fuck");
        assertThat(result.get(0).getAge()).isEqualTo(11);
        assertThat(result.get(0).getBloodType()).isEqualTo("A");


    }

    @Test
    void findByBloodType(){

        List<Person> result = personRepository.findByBloodType("A");
        assertThat(result.size()).isEqualTo(2);

    }

    @Test
    void findByMonthOfBirthday(){

        List<Person> result = personRepository.findByMonthOfBirthday(8);

        assertThat(result.size()).isEqualTo(2);
    }
}
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
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Test
    void findByName(){
        List<Person> people = personRepository.findByName("ff");

        assertThat(people.size()).isEqualTo(1);
        Person person = people.get(0);
        assertAll(
                () -> assertThat(person.getName()).isEqualTo("ff"),
                () -> assertThat(person.getHobby()).isEqualTo("reading"),
                () -> assertThat(person.getAddress()).isEqualTo("seoul"),
                () -> assertThat(person.getBirthday()).isEqualTo(Birthday.of(LocalDate.of(1991,7,10))),
                () -> assertThat(person.getJob()).isEqualTo("officer"),
                () -> assertThat(person.getPhoneNumber()).isEqualTo("010-1234-5678"),
                () -> assertThat(person.isDeleted()).isEqualTo(false)
        );
    }

    @Test
    void findByNameIfDeleted(){
        List<Person> people = personRepository.findByName("gg");

        assertThat(people.size()).isEqualTo(0);
    }

    @Test
    void findByMonthOfBirthday(){
        List<Person> people = personRepository.findByMonthOfBirthday(7);

        assertThat(people.size()).isEqualTo(2);
        assertAll(
                () -> assertThat(people.get(0).getName()).isEqualTo("bb"),
                () -> assertThat(people.get(1).getName()).isEqualTo("ff")
        );
    }

    @Test
    void findPeopleDeleted(){
        List<Person> people = personRepository.findPeopleDeleted();

        assertThat(people.size()).isEqualTo(1);
        assertThat(people.get(0).getName()).isEqualTo("gg");
    }

}
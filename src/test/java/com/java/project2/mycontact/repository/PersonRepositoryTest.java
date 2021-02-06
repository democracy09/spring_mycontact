package com.java.project2.mycontact.repository;

import com.java.project2.mycontact.domain.Person;
import com.java.project2.mycontact.domain.dto.Birthday;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
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

    @Test
    void findByBloodType(){
        givenPerson("aa", 10, "A");
        givenPerson("bb", 9, "B");
        givenPerson("cc", 8 ,"O");
        givenPerson("dd", 7, "AB");
        givenPerson("ee", 6 , "A");

        List<Person> result = personRepository.findByBloodType("A");

        result.forEach(System.out::println);
    }

    @Test
    void findByBirthDayBetween(){
        givenPerson("aa", 10, "A", LocalDate.of(1991,8,15));
        givenPerson("bb", 9, "B", LocalDate.of(1992,7,10));
        givenPerson("cc", 8 ,"O", LocalDate.of(1993,1,5));
        givenPerson("dd", 7, "AB", LocalDate.of(1994,8,30));

        List<Person> result = personRepository.findByMonthOfBirthday(8);

        result.forEach(System.out::println);
    }

    private void givenPerson(String name, int age, String bloodType){
        givenPerson(name, age, bloodType,null);
    }

    private void givenPerson(String name, int age, String bloodType, LocalDate birthday){
        Person person = new Person(name, age, bloodType);
        person.setBirthday(new Birthday(birthday.getYear(), birthday.getMonthValue(), birthday.getDayOfMonth()));

        personRepository.save(person);
    }
}
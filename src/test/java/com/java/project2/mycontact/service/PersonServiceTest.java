package com.java.project2.mycontact.service;

import com.java.project2.mycontact.domain.Block;
import com.java.project2.mycontact.domain.Person;
import com.java.project2.mycontact.repository.BlockRepository;
import com.java.project2.mycontact.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;


@SpringBootTest
class PersonServiceTest {

    @Autowired
    private PersonService personService;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private BlockRepository blockRepository;

    @Test
    void getPeopleExcludesBlocks(){
        givenPeople();
        givenBlocks();

        List<Person> result = personService.getPeopleExcludeBlocks();

        result.forEach(System.out::println);
    }

    @Test
    void cascadeTest(){
        givenPeople();

        List<Person> result = personRepository.findAll();

        result.forEach(System.out::println);

        Person person = result.get(3);
        person.getBlock().setStartDate(LocalDate.now());
        person.getBlock().setEndDate(LocalDate.now());

        personRepository.save(person);
        personRepository.findAll().forEach(System.out::println);


        person.setBlock(null);
        personRepository.save(person);
        personRepository.findAll().forEach(System.out::println);
        blockRepository.findAll().forEach(System.out::println);
    }

    @Test
    void getPerson(){
        givenPeople();

        Person person = personService.getPerson(3L);
        System.out.println(person);

    }

    private void givenBlocks() {
        givenBlock("dd");
        
    
    }

    private Block givenBlock(String name) {
        return blockRepository.save(new Block(name));

    }

    private void givenPeople() {
        givenPerson("dd",10,"A");
        givenPerson("ff",5,"B");
        givenBlockPerson("aa",8,"AB");
        givenBlockPerson("tt",15,"O");

    }

    private void givenPerson(String name, int age, String bloodType) {
        personRepository.save(new Person(name, age, bloodType));
    }

    private void givenBlockPerson(String name, int age, String bloodType){
        Person blockPerson = new Person(name, age, bloodType);
        blockPerson.setBlock(new Block(name));
        personRepository.save(blockPerson);
    }


}
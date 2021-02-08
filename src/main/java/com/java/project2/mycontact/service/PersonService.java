package com.java.project2.mycontact.service;


import com.java.project2.mycontact.domain.Block;
import com.java.project2.mycontact.domain.Person;
import com.java.project2.mycontact.repository.BlockRepository;
import com.java.project2.mycontact.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PersonService {
    
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private BlockRepository blockRepository;

    public List<Person> getPeopleExcludeBlocks(){

        return personRepository.findByBlockIsNull();
    }

    public List<Person> getPeopleByName(String name){

        return personRepository.findByName(name);
    }



    @Transactional(readOnly = true)
    public Person getPerson(Long id){
//        Person person = personRepository.findById(id).get();
        Person person = personRepository.findById(id).orElse(null);

        log.info("person : {}",person);

        return person;
    }

    @Transactional
    public void put(Person person){
        personRepository.save(person);
    }

    @Transactional
    public void modify(Long id, Person person) {
        Person dbPerson = personRepository.findById(id).orElseThrow(()-> new RuntimeException("속보)아이디가 존재안해…충격"));

        dbPerson.setName(person.getName())
                .setAge(person.getAge())
                .setPhoneNumber(person.getPhoneNumber())
                .setBirthday(person.getBirthday())
                .setJob(person.getJob())
                .setHobby(person.getHobby())
                .setAddress(person.getAddress())
                .setBlock(person.getBlock())
                .setBloodType(person.getBloodType());

        personRepository.save(dbPerson);
    }
}

package com.java.project2.mycontact.service;

import com.java.project2.mycontact.controller.dto.PersonDto;
import com.java.project2.mycontact.domain.Person;
import com.java.project2.mycontact.exception.PersonNotFoundException;
import com.java.project2.mycontact.exception.RenameNotPermittedException;
import com.java.project2.mycontact.repository.PersonRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {
    @InjectMocks        //테스트대상 클래스 autowired
    private PersonService personService;
    @Mock               //mock으로 만들어서 injection
    private PersonRepository personRepository;

    @Test
    void getAll(){
        when(personRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Lists.newArrayList(new Person("aa"), new Person("bb"), new Person("cc"))));

        Page<Person> result = personService.getAll(PageRequest.of(0, 3));

        assertThat(result.getNumberOfElements()).isEqualTo(3);
        assertThat(result.getContent().get(0).getName()).isEqualTo("aa");
        assertThat(result.getContent().get(1).getName()).isEqualTo("bb");
        assertThat(result.getContent().get(2).getName()).isEqualTo("cc");
    }

    @Test
    void getPeopleByName(){
        when(personRepository.findByName("aa"))
                .thenReturn(Lists.newArrayList(new Person("aa")));

        List<Person> result = personService.getPeopleByName("aa");

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getName()).isEqualTo("aa");
    }

    @Test
    void getPerson(){
        when(personRepository.findById(1L))
                .thenReturn(Optional.of(new Person("aa")));

        Person person = personService.getPerson(1L);

        assertThat(person.getName()).isEqualTo("aa");
    }

    @Test
    void getPersonIfNotFound(){
        when(personRepository.findById(1L))
                .thenReturn(Optional.empty());

        Person person = personService.getPerson(1L);

        assertThat(person).isNull();
    }

    @Test
    void put(){
        personService.put(mockPersonDto());

        verify(personRepository,times(1)).save(argThat(new IsPersonWillBeInserted()));
    }

    @Test
    void modifyIfPersonNotFound(){
        when(personRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(PersonNotFoundException.class, () -> personService.modify(1L, mockPersonDto()));
    }

    @Test
    void modifyIfNameIsDiff(){
        when(personRepository.findById(1L))
                .thenReturn(Optional.of(new Person("bb")));

        assertThrows(RenameNotPermittedException.class, () ->personService.modify(1L, mockPersonDto()));
    }

    @Test
    void modify(){
        when(personRepository.findById(1L))
                .thenReturn(Optional.of(new Person("aa")));

        personService.modify(1L, mockPersonDto());

        verify(personRepository, times(1)).save(argThat(new IsPersonWillBeUpdated()));
    }

    @Test
    void modifyByNameIfPersonNotFound(){
        when(personRepository.findById(1L))
            .thenReturn(Optional.empty());

        assertThrows(PersonNotFoundException.class, () -> personService.modify(1L, "bb"));
    }

    @Test
    void modifyByName(){
        when(personRepository.findById(1L))
                .thenReturn(Optional.of(new Person("aa")));

        personService.modify(1L, "bb");

        verify(personRepository, times(1)).save(argThat(new IsNameWillBeUpdated()));
    }

    @Test
    void deleteIfPersonNotFound(){
        when(personRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(PersonNotFoundException.class, () -> personService.delete(1L));
    }

    @Test
    void delete(){
        when(personRepository.findById(1L))
                .thenReturn(Optional.of(new Person("aa")));

        personService.delete(1L);

        verify(personRepository, times(1)).save(argThat(new IsPersonWillBeDeleted()));
    }

    private PersonDto mockPersonDto(){
        return PersonDto.of("aa","programming","seoul", LocalDate.now(), "programmer","010-2222-2222");
    }

    private static class IsPersonWillBeInserted implements ArgumentMatcher<Person>{
        @Override
        public boolean matches(Person person) {
            return equals(person.getName(),"aa")
                    && equals(person.getHobby(),"programming")
                    && equals(person.getAddress(),"seoul")
                    && equals(person.getJob(),"programmer")
                    && equals(person.getPhoneNumber(),"010-2222-2222");
        }

        private boolean equals(String actual, String expected){
            return expected.equals(actual);
        }
    }

    private static class IsPersonWillBeUpdated implements ArgumentMatcher<Person>{
        @Override
        public boolean matches(Person person) {
            return equals(person.getName(),"aa")
                    && equals(person.getHobby(),"programming")
                    && equals(person.getAddress(),"seoul")
                    && equals(person.getJob(),"programmer")
                    && equals(person.getPhoneNumber(),"010-2222-2222");
        }

        private boolean equals(String actual, String expected){
            return expected.equals(actual);
        }
    }

    private static class IsNameWillBeUpdated implements ArgumentMatcher<Person>{
        @Override
        public boolean matches(Person person) {
            return person.getName().equals("bb");
        }
    }

    private static class IsPersonWillBeDeleted implements ArgumentMatcher<Person>{

        @Override
        public boolean matches(Person person) {
            return person.isDeleted();
        }
    }


}
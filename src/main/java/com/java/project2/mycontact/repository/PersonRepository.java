package com.java.project2.mycontact.repository;


import com.java.project2.mycontact.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PersonRepository extends JpaRepository<Person,Long> {
}

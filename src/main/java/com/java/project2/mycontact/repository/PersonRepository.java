package com.java.project2.mycontact.repository;


import com.java.project2.mycontact.domaion.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface PersonRepository extends JpaRepository<Person,Long> {
}

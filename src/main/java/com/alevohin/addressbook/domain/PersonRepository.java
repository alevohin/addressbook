
package com.alevohin.addressbook.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findByNameLikeIgnoreCaseAndUsernameOrderById(String nameFilter, String username);
}

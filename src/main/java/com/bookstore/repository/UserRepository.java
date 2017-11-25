package com.bookstore.repository;

import com.bookstore.domain.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by User on 16.11.2017.
 */
public interface UserRepository extends CrudRepository<User,Long>{
    User findByUsername(String username);

    User findByEmail(String email);
}

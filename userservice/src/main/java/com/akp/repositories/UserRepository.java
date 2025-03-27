package com.akp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.akp.entities.User;

public interface UserRepository extends JpaRepository<User, String>{

}

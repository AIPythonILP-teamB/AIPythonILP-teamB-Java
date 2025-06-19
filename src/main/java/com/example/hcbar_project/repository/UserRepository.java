package com.example.hcbar_project.repository;

import com.example.hcbar_project.model.User;


import java.util.List;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  //Optionalでとういつするやつやるぞ。
    User findByEmail(String email);
     Optional<User> findByEmail(String email);
     List<User> findByIsActiveTrueOrderByIdAsc();
　　 boolean existsByEmail(String email);
  　　User findByUserName(String username);
}


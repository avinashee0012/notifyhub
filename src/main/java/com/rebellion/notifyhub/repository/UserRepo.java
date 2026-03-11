package com.rebellion.notifyhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rebellion.notifyhub.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long>{
}

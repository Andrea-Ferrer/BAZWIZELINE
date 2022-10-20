package com.wizeline.learningjava.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.wizeline.learningjava.model.UserDTO;

public interface UserRepository extends MongoRepository<UserDTO, String> {
}

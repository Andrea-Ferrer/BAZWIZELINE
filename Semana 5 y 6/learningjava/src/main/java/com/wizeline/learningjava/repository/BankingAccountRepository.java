package com.wizeline.learningjava.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.wizeline.learningjava.model.BankAccountDTO;

/**
 * Class Description goes here.
 * Created by enrique.gutierrez on 27/09/22
 */
@Repository
public interface BankingAccountRepository extends MongoRepository<BankAccountDTO, Long> {
}
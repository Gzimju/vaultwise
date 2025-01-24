package com.vaultwise.repository;

import com.vaultwise.model.Card;
import com.vaultwise.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByAccount(Account account);  // Custom query to find cards by account
}

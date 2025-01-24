package com.vaultwise.repository;

import com.vaultwise.model.Account;
import com.vaultwise.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByAccount(Account account);
}


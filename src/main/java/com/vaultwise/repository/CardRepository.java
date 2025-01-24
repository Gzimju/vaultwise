package com.vaultwise.repository;

import com.vaultwise.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    // Custom queries can be added here if needed
}

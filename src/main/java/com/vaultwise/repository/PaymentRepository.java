package com.vaultwise.repository;

import com.vaultwise.model.Payment;
import com.vaultwise.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByAccount(Account account);  // Custom query to find payments by account
}

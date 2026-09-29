package com.inventory.rawmaterial.repository;

import com.inventory.rawmaterial.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}

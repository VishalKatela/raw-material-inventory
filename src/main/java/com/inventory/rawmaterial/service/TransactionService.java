package com.inventory.rawmaterial.service;

import com.inventory.rawmaterial.model.Material;
import com.inventory.rawmaterial.model.Transaction;
import com.inventory.rawmaterial.repository.MaterialRepository;
import com.inventory.rawmaterial.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final MaterialRepository materialRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              MaterialRepository materialRepository) {
        this.transactionRepository = transactionRepository;
        this.materialRepository = materialRepository;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction processTransaction(
            Integer materialId,
            String transactionType,
            BigDecimal quantity,
            String remarks) {

        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("Material not found"));

        BigDecimal currentQuantity = material.getQuantity();

        if ("STOCK_IN".equalsIgnoreCase(transactionType)) {
            material.setQuantity(currentQuantity.add(quantity));
        } else if ("STOCK_OUT".equalsIgnoreCase(transactionType)) {

            if (currentQuantity.compareTo(quantity) < 0) {
                throw new RuntimeException("Insufficient stock");
            }

            material.setQuantity(currentQuantity.subtract(quantity));
        } else {
            throw new RuntimeException("Invalid transaction type");
        }

        if (material.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
            material.setStatus("OUT_OF_STOCK");
        } else if (material.getQuantity().compareTo(material.getMinimumQuantity()) <= 0) {
            material.setStatus("LOW_STOCK");
        } else {
            material.setStatus("IN_STOCK");
        }

        materialRepository.save(material);

        Transaction transaction = new Transaction();
        transaction.setMaterial(material);
        transaction.setTransactionType(transactionType.toUpperCase());
        transaction.setQuantity(quantity);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setRemarks(remarks);

        return transactionRepository.save(transaction);
    }
}

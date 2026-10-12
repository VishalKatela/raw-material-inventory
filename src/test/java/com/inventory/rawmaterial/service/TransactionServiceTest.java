package com.inventory.rawmaterial.service;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.inventory.rawmaterial.model.Material;
import com.inventory.rawmaterial.repository.MaterialRepository;
import com.inventory.rawmaterial.repository.TransactionRepository;

@SpringBootTest
class TransactionServiceTest {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private Integer materialId;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
        materialRepository.deleteAll();

        Material material = new Material();
        material.setMaterialCode("TX-TEST-100");
        material.setName("Transaction Test Material");
        material.setCategory("Test");
        material.setUnit("kg");
        material.setQuantity(new BigDecimal("10"));
        material.setMinimumQuantity(new BigDecimal("5"));
        materialId = materialService.addMaterial(material).getId();
    }

    @Test
    void shouldApplyStockInAndStockOutAndUpdateStatus() {
        transactionService.processTransaction(
                materialId, "STOCK_IN", new BigDecimal("10"), "Received stock");
        transactionService.processTransaction(
                materialId, "STOCK_OUT", new BigDecimal("16"), "Issued stock");

        Material updatedMaterial = materialService.getMaterialById(materialId);

        assertThat(updatedMaterial.getQuantity()).isEqualByComparingTo("4");
        assertThat(updatedMaterial.getStatus()).isEqualTo("LOW_STOCK");
        assertThat(transactionRepository.count()).isEqualTo(2);
    }

    @Test
    void shouldRejectOverdrawWithoutChangingStockOrHistory() {
        assertThatThrownBy(() -> transactionService.processTransaction(
                materialId, "STOCK_OUT", new BigDecimal("11"), "Too much"))
                .hasMessage("Insufficient stock");

        assertThat(materialService.getMaterialById(materialId).getQuantity())
                .isEqualByComparingTo("10");
        assertThat(transactionRepository.count()).isZero();
    }

    @Test
    void shouldRejectNonpositiveQuantitiesAndUnknownTypes() {
        assertThatThrownBy(() -> transactionService.processTransaction(
                materialId, "STOCK_IN", BigDecimal.ZERO, "Zero"))
                .hasMessage("Quantity must be greater than zero");
        assertThatThrownBy(() -> transactionService.processTransaction(
                materialId, "STOCK_OUT", new BigDecimal("-2"), "Negative"))
                .hasMessage("Quantity must be greater than zero");
        assertThatThrownBy(() -> transactionService.processTransaction(
                materialId, "ADJUSTMENT", BigDecimal.ONE, "Unknown"))
                .hasMessage("Invalid transaction type");

        assertThat(materialService.getMaterialById(materialId).getQuantity())
                .isEqualByComparingTo("10");
        assertThat(transactionRepository.count()).isZero();
    }
}
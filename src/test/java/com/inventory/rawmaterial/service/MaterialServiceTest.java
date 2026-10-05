package com.inventory.rawmaterial.service;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.inventory.rawmaterial.model.Material;
import com.inventory.rawmaterial.repository.MaterialRepository;

@SpringBootTest
class MaterialServiceTest {

    @Autowired
    private MaterialService materialService;

    @Autowired
    private MaterialRepository materialRepository;

    @BeforeEach
    void setUp() {
        materialRepository.deleteAll();
    }

    @Test
    void shouldComputeLowStockStatus() {
        Material material = new Material();
        material.setMaterialCode("MAT-100");
        material.setName("Steel Rod");
        material.setCategory("Metals");
        material.setUnit("kg");
        material.setQuantity(new BigDecimal("10"));
        material.setMinimumQuantity(new BigDecimal("15"));

        Material saved = materialService.addMaterial(material);

        assertThat(saved.getStatus()).isEqualTo("LOW_STOCK");
    }

    @Test
    void shouldFilterMaterialsByKeywordAndStatus() {
        Material steel = new Material();
        steel.setMaterialCode("MAT-101");
        steel.setName("Steel Plate");
        steel.setCategory("Metals");
        steel.setUnit("kg");
        steel.setQuantity(new BigDecimal("50"));
        steel.setMinimumQuantity(new BigDecimal("10"));

        Material copper = new Material();
        copper.setMaterialCode("MAT-102");
        copper.setName("Copper Cable");
        copper.setCategory("Electrical");
        copper.setUnit("m");
        copper.setQuantity(new BigDecimal("12"));
        copper.setMinimumQuantity(new BigDecimal("5"));

        materialService.addMaterial(steel);
        materialService.addMaterial(copper);

        List<Material> results = materialService.searchMaterials("steel", "IN_STOCK", "Metals");

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getMaterialCode()).isEqualTo("MAT-101");
    }
}

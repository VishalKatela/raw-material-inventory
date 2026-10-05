package com.inventory.rawmaterial.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.inventory.rawmaterial.model.Material;
import com.inventory.rawmaterial.repository.MaterialRepository;

@Service
public class MaterialService {

    private final MaterialRepository materialRepository;

    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
    }

    public Material getMaterialById(Integer id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material not found"));
    }

    public Material addMaterial(Material material) {

        if (material.getMaterialCode() == null ||
                material.getMaterialCode().isBlank()) {
            throw new RuntimeException("Material code is required");
        }

        if (material.getName() == null ||
                material.getName().isBlank()) {
            throw new RuntimeException("Material name is required");
        }

        if (material.getQuantity() == null) {
            material.setQuantity(BigDecimal.ZERO);
        }

        if (material.getMinimumQuantity() == null) {
            material.setMinimumQuantity(BigDecimal.ZERO);
        }

        updateStatus(material);

        return materialRepository.save(material);
    }

    public Material updateMaterial(Integer id, Material updatedMaterial) {

        Material material = getMaterialById(id);

        material.setMaterialCode(updatedMaterial.getMaterialCode());
        material.setName(updatedMaterial.getName());
        material.setCategory(updatedMaterial.getCategory());
        material.setUnit(updatedMaterial.getUnit());
        material.setQuantity(updatedMaterial.getQuantity());
        material.setMinimumQuantity(updatedMaterial.getMinimumQuantity());

        updateStatus(material);

        return materialRepository.save(material);
    }

    public List<Material> searchMaterials(String keyword, String status, String category) {
        List<Material> all = materialRepository.findAll();
        List<Material> results = new ArrayList<>();

        for (Material material : all) {
            boolean matchesKeyword = keyword == null || keyword.isBlank() ||
                    material.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                    material.getMaterialCode().toLowerCase().contains(keyword.toLowerCase());

            boolean matchesStatus = status == null || status.isBlank() ||
                    status.equalsIgnoreCase(material.getStatus());

            boolean matchesCategory = category == null || category.isBlank() ||
                    (material.getCategory() != null && material.getCategory().equalsIgnoreCase(category));

            if (matchesKeyword && matchesStatus && matchesCategory) {
                results.add(material);
            }
        }

        return results;
    }

    private void updateStatus(Material material) {

        if (material.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
            material.setStatus("OUT_OF_STOCK");

        } else if (material.getQuantity()
                .compareTo(material.getMinimumQuantity()) <= 0) {
            material.setStatus("LOW_STOCK");

        } else {
            material.setStatus("IN_STOCK");
        }
    }
}
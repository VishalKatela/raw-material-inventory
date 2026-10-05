package com.inventory.rawmaterial.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.rawmaterial.model.Material;
import com.inventory.rawmaterial.service.MaterialService;

@RestController
@RequestMapping("/api/materials")
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @GetMapping
    public List<Material> getAllMaterials() {
        return materialService.getAllMaterials();
    }

    @GetMapping("/{id}")
    public Material getMaterialById(@PathVariable Integer id) {
        return materialService.getMaterialById(id);
    }

    @GetMapping("/search")
    public List<Material> searchMaterials(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String category) {
        return materialService.searchMaterials(keyword, status, category);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Material addMaterial(@RequestBody Material material) {
        return materialService.addMaterial(material);
    }

    @PutMapping("/{id}")
    public Material updateMaterial(
            @PathVariable Integer id,
            @RequestBody Material material) {

        return materialService.updateMaterial(id, material);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleRuntimeException(RuntimeException ex) {
        return Map.of("error", ex.getMessage());
    }
}
package com.inventory.rawmaterial.controller;

import com.inventory.rawmaterial.model.Material;
import com.inventory.rawmaterial.service.MaterialService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
package myboot.app3.backoffice.controller;


import myboot.app3.backoffice.entity.Creancier;
import myboot.app3.backoffice.service.CreancierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/client/creanciers")
public class CreancierController {

    @Autowired
    private CreancierService creancierService;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllCreanciers() {
        List<Creancier> creanciers = creancierService.getAllCreanciers();
        List<Map<String, Object>> response = creanciers.stream().map(creancier -> {
            Map<String, Object> creancierMap = new HashMap<>();
            creancierMap.put("id", creancier.getId());
            creancierMap.put("nom", creancier.getNom());
            creancierMap.put("categorie", creancier.getCategorie());
            creancierMap.put("logo", creancier.getLogo());
            creancierMap.put("produits", creancier.getProduits());
            return creancierMap;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getCreancierById(@PathVariable Long id) {
        Optional<Creancier> creancier = creancierService.getCreancierById(id);
        if (creancier.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("id", creancier.get().getId());
            response.put("nom", creancier.get().getNom());
            response.put("categorie", creancier.get().getCategorie());
            response.put("logo", creancier.get().getLogo());
            response.put("produits", creancier.get().getProduits());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Creancier createCreancier(@RequestBody Creancier creancier) {
        return creancierService.createCreancier(creancier);
    }

    @PutMapping("/{id}")
    public Creancier updateCreancier(@PathVariable Long id, @RequestBody Creancier creancier) {
        return creancierService.updateCreancier(id, creancier);
    }

    @DeleteMapping("/{id}")
    public void deleteCreancier(@PathVariable Long id) {
        creancierService.deleteCreancier(id);
    }
}
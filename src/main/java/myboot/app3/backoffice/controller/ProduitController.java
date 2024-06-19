package myboot.app3.backoffice.controller;

import myboot.app3.backoffice.dto.ProductDetailsDto;
import myboot.app3.backoffice.entity.Produit;
import myboot.app3.backoffice.exceptions.ResourceNotFoundException;
import myboot.app3.backoffice.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/client/products")
public class ProduitController {

    @Autowired
    private ProduitService produitService;

    @GetMapping
    public ResponseEntity<List<Produit>> getAllProduits() {
        List<Produit> produits = produitService.findAll();
        return ResponseEntity.ok(produits);
    }

    @GetMapping(value = "/{id}/details")
    public ResponseEntity<?> getProductDetails(@PathVariable Long id) {
        return produitService.getById(id)
                .map(produit -> ResponseEntity.ok(produit))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}/unpaid")
    public List<Produit> getUnpaidProduitsByUser(@PathVariable Long userId) {
        return produitService.getUnpaidProduitsByUser(userId);
    }

    @GetMapping("/paid")
    public ResponseEntity<List<ProductDetailsDto>> getPaidProduits() {
        List<ProductDetailsDto> produits = produitService.getPaidProduits();
        return ResponseEntity.ok(produits);
    }

    @PutMapping("/user/{userId}/markAsPaid/{id}")
    public Produit markAsPaid(@PathVariable Long userId, @PathVariable Long id) {
        Produit produit = produitService.getProduitById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit not found with id " + id));

        if (!produit.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Produit not found for user with id " + userId);
        }
        return produitService.markAsPaid(id);
    }
}
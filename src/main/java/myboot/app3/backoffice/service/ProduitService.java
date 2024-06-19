package myboot.app3.backoffice.service;


import myboot.app3.backoffice.dto.ProductDetailsDto;
import myboot.app3.backoffice.entity.Produit;
import myboot.app3.backoffice.exceptions.ResourceNotFoundException;
import myboot.app3.backoffice.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProduitService {

    @Autowired
    private ProduitRepository produitRepository;

    public Optional<Produit> getProduitById(Long id) {
        return produitRepository.findById(id);
    }

    public List<Produit> findAll() {
        return produitRepository.findAll();
    }

    public Optional<Produit> getById(Long id) {
        return produitRepository.findById(id);
    }

    public List<Produit> getUnpaidProduitsByUser(Long userId) {
        return produitRepository.findByUserIdAndUnpaid(userId, true);
    }

    public Produit markAsPaid(Long id) {
        Produit produit = produitRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produit not found"));
        produit.setUnpaid(false); // Marquer comme payé
        return produitRepository.save(produit);
    }

    public List<ProductDetailsDto> getPaidProduits() {
        List<Produit> produits = produitRepository.findByUnpaid(false);
        return produits.stream()
                .map(ProductDetailsDto::new)
                .collect(Collectors.toList());
    }

    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }
}
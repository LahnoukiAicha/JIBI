package myboot.app3.backoffice.service;


import myboot.app3.backoffice.entity.Creancier;
import myboot.app3.backoffice.repository.CreancierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CreancierService {

    @Autowired
    private CreancierRepository creancierRepository;

    public List<Creancier> getAllCreanciers() {
        return creancierRepository.findAll();
    }

    public Optional<Creancier> getCreancierById(Long id) {
        return creancierRepository.findById(id);
    }

    public Creancier createCreancier(Creancier creancier) {
        return creancierRepository.save(creancier);
    }

    public Creancier updateCreancier(Long id, Creancier creancier) {
        return creancierRepository.findById(id)
                .map(existingCreancier -> {
                    existingCreancier.setNom(creancier.getNom());
                    existingCreancier.setCategorie(creancier.getCategorie());
                    existingCreancier.setLogo(creancier.getLogo());
                    existingCreancier.setProduits(creancier.getProduits());
                    return creancierRepository.save(existingCreancier);
                }).orElseGet(() -> {
                    creancier.setId(id);
                    return creancierRepository.save(creancier);
                });
    }

    public void deleteCreancier(Long id) {
        creancierRepository.deleteById(id);
    }
}

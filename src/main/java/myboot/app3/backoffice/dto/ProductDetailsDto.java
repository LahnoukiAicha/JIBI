package myboot.app3.backoffice.dto;


import myboot.app3.backoffice.entity.Produit;

public class ProductDetailsDto {
    private Long id;
    private String nom;
    private String creancierNom;
    private String creancierLogo;

    public ProductDetailsDto(Produit produit) {
        this.id = produit.getId();
        this.nom = produit.getNom();
        this.creancierNom = produit.getCreancier().getNom();
        this.creancierLogo = produit.getCreancier().getLogo();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCreancierNom() {
        return creancierNom;
    }

    public void setCreancierNom(String creancierNom) {
        this.creancierNom = creancierNom;
    }

    public String getCreancierLogo() {
        return creancierLogo;
    }

    public void setCreancierLogo(String creancierLogo) {
        this.creancierLogo = creancierLogo;
    }
}
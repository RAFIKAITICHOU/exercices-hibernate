package ma.projet.service;

import ma.projet.classes.Categorie;
import ma.projet.classes.Commande;
import ma.projet.classes.LigneCommandeProduit;
import ma.projet.classes.Produit;
import ma.projet.util.Dates;

import java.time.LocalDate;
import java.util.List;

public class ProduitService extends AbstractFacade<Produit> {

    public ProduitService() {
        super(Produit.class);
    }

    /** Produits d'une catégorie. */
    public List<Produit> findByCategorie(int categorieId) {
        return read(s -> s.createQuery(
                "from Produit p where p.categorie.id = :cid order by p.reference", Produit.class)
                .setParameter("cid", categorieId)
                .list());
    }

    public void afficherParCategorie(int categorieId) {
        Categorie cat = read(s -> s.get(Categorie.class, categorieId));
        System.out.println("Catégorie : " + (cat == null ? categorieId : cat.getLibelle()));
        System.out.printf("%-12s %s%n", "Référence", "Prix");
        for (Produit p : findByCategorie(categorieId)) {
            System.out.printf("%-12s %.0f DH%n", p.getReference(), p.getPrix());
        }
    }

    /** Produits commandés entre deux dates (dates de commande). */
    public List<Produit> findCommandesEntre(LocalDate d1, LocalDate d2) {
        return read(s -> s.createQuery(
                "select distinct l.produit from LigneCommandeProduit l "
                        + "where l.commande.dateCommande between :d1 and :d2 order by l.produit.reference",
                Produit.class)
                .setParameter("d1", d1)
                .setParameter("d2", d2)
                .list());
    }

    public void afficherCommandesEntre(LocalDate d1, LocalDate d2) {
        System.out.println("Produits commandés entre le " + Dates.courte(d1) + " et le " + Dates.courte(d2) + " :");
        for (Produit p : findCommandesEntre(d1, d2)) {
            System.out.printf("%-12s %.0f DH%n", p.getReference(), p.getPrix());
        }
    }

    /** Lignes (produit + quantité) d'une commande donnée. */
    public List<LigneCommandeProduit> findByCommande(int commandeId) {
        return read(s -> s.createQuery(
                "select l from LigneCommandeProduit l join fetch l.produit join fetch l.commande "
                        + "where l.commande.id = :id order by l.id", LigneCommandeProduit.class)
                .setParameter("id", commandeId)
                .list());
    }

    /** Affichage au format de l'énoncé. */
    public void afficherParCommande(int commandeId) {
        List<LigneCommandeProduit> lignes = findByCommande(commandeId);
        if (lignes.isEmpty()) {
            System.out.println("Commande " + commandeId + " : aucune ligne.");
            return;
        }
        Commande c = lignes.get(0).getCommande();
        System.out.println("Commande : " + c.getId() + "     Date : " + Dates.longue(c.getDateCommande()));
        System.out.println("Liste des produits :");
        System.out.printf("%-12s %-10s %s%n", "Référence", "Prix", "Quantité");
        for (LigneCommandeProduit l : lignes) {
            System.out.printf("%-12s %-10s %d%n", l.getProduit().getReference(),
                    String.format("%.0f DH", l.getProduit().getPrix()), l.getQuantite());
        }
    }

    /** Requête nommée : produits dont le prix est strictement supérieur à un seuil. */
    public List<Produit> findByPrixSuperieur(float prix) {
        return read(s -> s.createNamedQuery("Produit.findByPrixSup", Produit.class)
                .setParameter("prix", prix)
                .list());
    }

    public void afficherPrixSuperieur(float prix) {
        System.out.printf("Produits dont le prix est supérieur à %.0f DH :%n", prix);
        for (Produit p : findByPrixSuperieur(prix)) {
            System.out.printf("%-12s %.0f DH%n", p.getReference(), p.getPrix());
        }
    }
}

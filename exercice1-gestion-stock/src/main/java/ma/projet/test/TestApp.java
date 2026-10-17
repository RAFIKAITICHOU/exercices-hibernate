package ma.projet.test;

import ma.projet.classes.*;
import ma.projet.service.*;
import ma.projet.util.HibernateUtil;

import java.time.LocalDate;

/** Programme de test : insère des données puis valide chaque fonctionnalité. */
public class TestApp {

    public static Categorie catInfo, catAcc;
    public static Produit es12, zr85, ee85, xy10;
    public static Commande commande1, commande2;

    public static void seed() {
        CategorieService categorieService = new CategorieService();
        ProduitService produitService = new ProduitService();
        CommandeService commandeService = new CommandeService();
        LigneCommandeService ligneService = new LigneCommandeService();

        catInfo = new Categorie("INFO", "Informatique");
        catAcc = new Categorie("ACC", "Accessoires");
        categorieService.create(catInfo);
        categorieService.create(catAcc);

        es12 = new Produit("ES12", 120f, catInfo);
        zr85 = new Produit("ZR85", 100f, catInfo);
        ee85 = new Produit("EE85", 200f, catInfo);
        xy10 = new Produit("XY10", 80f, catAcc);
        produitService.create(es12);
        produitService.create(zr85);
        produitService.create(ee85);
        produitService.create(xy10);

        commande1 = new Commande(LocalDate.of(2013, 3, 14));
        commande2 = new Commande(LocalDate.of(2013, 4, 2));
        commandeService.create(commande1);
        commandeService.create(commande2);

        ligneService.create(new LigneCommandeProduit(commande1, es12, 7));
        ligneService.create(new LigneCommandeProduit(commande1, zr85, 14));
        ligneService.create(new LigneCommandeProduit(commande1, ee85, 5));
        ligneService.create(new LigneCommandeProduit(commande2, xy10, 3));
        ligneService.create(new LigneCommandeProduit(commande2, es12, 1));
    }

    public static void main(String[] args) {
        seed();
        ProduitService produitService = new ProduitService();

        System.out.println("===== 1. Produits par catégorie =====");
        produitService.afficherParCategorie(catInfo.getId());

        System.out.println("\n===== 2. Produits commandés entre deux dates =====");
        produitService.afficherCommandesEntre(LocalDate.of(2013, 3, 1), LocalDate.of(2013, 3, 31));

        System.out.println("\n===== 3. Produits d'une commande =====");
        produitService.afficherParCommande(commande1.getId());

        System.out.println("\n===== 4. Produits de prix > 100 DH (requête nommée) =====");
        produitService.afficherPrixSuperieur(100f);

        HibernateUtil.shutdown();
    }
}

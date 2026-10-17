package ma.projet.test;

import ma.projet.classes.LigneCommandeProduit;
import ma.projet.classes.Produit;
import ma.projet.service.ProduitService;
import ma.projet.util.HibernateUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class StockServiceTest {

    private static final ProduitService produitService = new ProduitService();

    @BeforeClass
    public static void init() { TestApp.seed(); }

    @AfterClass
    public static void end() { HibernateUtil.shutdown(); }

    @Test
    public void produitsParCategorie() {
        assertEquals(3, produitService.findByCategorie(TestApp.catInfo.getId()).size());
        assertEquals(1, produitService.findByCategorie(TestApp.catAcc.getId()).size());
    }

    @Test
    public void produitsCommandesEntreDeuxDates() {
        List<Produit> mars = produitService.findCommandesEntre(LocalDate.of(2013, 3, 1), LocalDate.of(2013, 3, 31));
        assertEquals(3, mars.size());
        List<Produit> avril = produitService.findCommandesEntre(LocalDate.of(2013, 4, 1), LocalDate.of(2013, 4, 30));
        assertEquals(2, avril.size());
    }

    @Test
    public void produitsDUneCommande() {
        List<LigneCommandeProduit> lignes = produitService.findByCommande(TestApp.commande1.getId());
        assertEquals(3, lignes.size());
        int total = lignes.stream().mapToInt(LigneCommandeProduit::getQuantite).sum();
        assertEquals(26, total);
    }

    @Test
    public void requeteNommeePrixSuperieurA100() {
        List<Produit> res = produitService.findByPrixSuperieur(100f);
        assertEquals(2, res.size());
        assertEquals("ES12", res.get(0).getReference());
        assertEquals("EE85", res.get(1).getReference());
    }
}

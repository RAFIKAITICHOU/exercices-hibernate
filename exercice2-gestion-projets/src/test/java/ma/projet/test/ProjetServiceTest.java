package ma.projet.test;

import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.service.EmployeService;
import ma.projet.service.ProjetService;
import ma.projet.service.TacheService;
import ma.projet.util.HibernateUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class ProjetServiceTest {

    private static final EmployeService employeService = new EmployeService();
    private static final ProjetService projetService = new ProjetService();
    private static final TacheService tacheService = new TacheService();

    @BeforeClass
    public static void init() { TestApp.seed(); }

    @AfterClass
    public static void end() { HibernateUtil.shutdown(); }

    @Test
    public void tachesRealiseesParEmploye() {
        List<EmployeTache> res = employeService.findTachesRealisees(TestApp.emp2.getId());
        assertEquals(2, res.size());
        assertEquals("Analyse", res.get(0).getTache().getNom());
    }

    @Test
    public void projetsGeresParEmploye() {
        List<Projet> res = employeService.findProjetsGeres(TestApp.emp1.getId());
        assertEquals(2, res.size());
        assertEquals(1, employeService.findProjetsGeres(TestApp.emp2.getId()).size());
    }

    @Test
    public void tachesPlanifieesDUnProjet() {
        assertEquals(3, projetService.findTachesPlanifiees(TestApp.projet1.getId()).size());
    }

    @Test
    public void tachesRealiseesAvecDatesReelles() {
        List<EmployeTache> res = projetService.findTachesRealisees(TestApp.projet1.getId());
        assertEquals(3, res.size());
        assertEquals(LocalDate.of(2013, 2, 10), res.get(0).getDateDebutReelle());
        assertEquals(LocalDate.of(2013, 2, 20), res.get(0).getDateFinReelle());
    }

    @Test
    public void requeteNommeeTachesChères() {
        List<Tache> res = tacheService.findByPrixSuperieur(1000);
        assertEquals(2, res.size());
    }

    @Test
    public void tachesRealiseesEntreDeuxDates() {
        List<Tache> res = tacheService.findRealiseesEntre(LocalDate.of(2013, 2, 1), LocalDate.of(2013, 3, 31));
        assertEquals(2, res.size());
    }
}

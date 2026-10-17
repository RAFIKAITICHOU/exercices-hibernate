package ma.projet.test;

import ma.projet.beans.Femme;
import ma.projet.service.FemmeService;
import ma.projet.service.HommeService;
import ma.projet.util.HibernateUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class EtatCivilServiceTest {

    private static final HommeService hommeService = new HommeService();
    private static final FemmeService femmeService = new FemmeService();

    @BeforeClass
    public static void init() { TestApp.seed(); }

    @AfterClass
    public static void end() { HibernateUtil.shutdown(); }

    @Test
    public void dixFemmesEtCinqHommes() {
        assertEquals(10, femmeService.findAll().size());
        assertEquals(5, hommeService.findAll().size());
    }

    @Test
    public void femmeLaPlusAgee() {
        Femme f = femmeService.findPlusAgee();
        assertEquals("CHRAIBI", f.getNom());   // née en 1965
    }

    @Test
    public void epousesEntreDeuxDates() {
        List<Femme> res = hommeService.findEpousesEntre(
                TestApp.hommes[0].getId(), LocalDate.of(1990, 1, 1), LocalDate.of(1996, 1, 1));
        assertEquals(3, res.size());   // Karima, Salima, Amal (Wafa épouse en 2000)
    }

    @Test
    public void nombreEnfantsEntreDeuxDates() {
        int n = femmeService.nombreEnfantsEntre(
                TestApp.femmes[0].getId(), LocalDate.of(1990, 1, 1), LocalDate.of(2000, 1, 1));
        assertEquals(4, n);
    }

    @Test
    public void femmesMarieesAuMoinsDeuxFois() {
        List<Femme> res = femmeService.findMarieesAuMoinsDeuxFois();
        assertEquals(1, res.size());
        assertEquals("CHRAIBI", res.get(0).getNom());
    }

    @Test
    public void hommesMariesAQuatreFemmes() {
        assertEquals(1, hommeService.compterHommesMariesAQuatreFemmes(
                LocalDate.of(1980, 1, 1), LocalDate.of(2010, 1, 1)));
        // sur une période courte, plus personne n'a 4 épouses
        assertEquals(0, hommeService.compterHommesMariesAQuatreFemmes(
                LocalDate.of(1989, 1, 1), LocalDate.of(1989, 12, 31)));
    }
}
